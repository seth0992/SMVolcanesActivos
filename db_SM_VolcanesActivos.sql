CREATE DATABASE db_SM_VolcanesActivos;
GO

USE db_SM_VolcanesActivos;
GO

CREATE TABLE Volcan(
	ID INT PRIMARY KEY IDENTITY,
	Nombre NVARCHAR(100) NOT NULL,
	Ubicacion NVARCHAR(255) NOT NULL,
	Altura INT CHECK(Altura > 0),
	Estado NVARCHAR(50) CHECK(Estado IN ('Activo', 'Inactivo','Alerta', 'Erupción')) DEFAULT 'Activo'
);
GO

CREATE TABLE Actividad_Sismica (
	ID INT PRIMARY KEY IDENTITY,
	Volcan_ID INT FOREIGN KEY REFERENCES Volcan(ID),
	Fecha DATETIME DEFAULT GETDATE(),
	Magnitud DECIMAL(3,1) CHECK (Magnitud >= 0),
	Profundidad INT CHECK(Profundidad >= 0)
);
GO

CREATE TABLE Erupcion(
	ID INT PRIMARY KEY IDENTITY,
	Volcan_ID INT FOREIGN KEY REFERENCES Volcan(ID),
	Fecha DATETIME DEFAULT GETDATE(),
	Tipo NVARCHAR(50) CHECK (Tipo IN ('Explosiva', 'Efusiva', 'Freatomagmática')),
	Duracion_Horas INT CHECK (Duracion_Horas > 0)
);
GO

CREATE TABLE Sensor(
	ID INT PRIMARY KEY IDENTITY,
	Volcan_ID INT FOREIGN KEY REFERENCES Volcan(ID),
	Ubicacion NVARCHAR(255),
	Estado NVARCHAR(50) CHECK (Estado IN ('Operativo', 'Falla', 'Reparación')) DEFAULT 'Operativo'
);
GO

-- Crear los SP
-- CRUD -> Volcan
-- Create -> Insertar un Volcan
CREATE PROCEDURE SPInsertarVolcan
	@Nombre NVARCHAR(100),
	@Ubicacion NVARCHAR(255),
	@Altura INT
AS
BEGIN

	INSERT INTO Volcan (Nombre,Ubicacion,Altura) 
	VALUES (@Nombre,@Ubicacion,@Altura)
	
END
GO

-- Read -> Cosultar Volcan (Todos o por (ID) o (Filtro))
CREATE PROCEDURE SPObtenerVolcanesConFiltro
	@Nombre NVARCHAR(100) = NULL,
	@Estado NVARCHAR(50) = NULL,
	@Altura INT = NULL
AS
BEGIN
	SELECT ID,Nombre,Ubicacion,Altura,Estado FROM Volcan
	WHERE (@Nombre IS NULL OR Nombre LIKE '%'+@Nombre+'%') 
		AND (@Estado IS NULL OR Estado = @Estado )
		AND (@Altura IS NULL OR Altura = @Altura)
END
GO
exec SPObtenerVolcanesConFiltro

CREATE PROCEDURE SPObtenerVolcanesPorID
	@ID INT
AS
BEGIN
	SELECT ID,Nombre,Ubicacion,Altura,Estado FROM Volcan
	WHERE ID = @ID
END
GO

EXEC SPObtenerVolcanesConFiltro
GO

-- Update -> Actualizar los datos del volcan
CREATE PROCEDURE SPActualizarVolcan
	@ID INT,
	@Nombre NVARCHAR(100),
	@Ubicacion NVARCHAR(255),
	@Altura INT,
	@Estado NVARCHAR(50)
AS
BEGIN
	UPDATE Volcan SET Nombre = @Nombre, Ubicacion = @Ubicacion, Altura = @Altura, Estado = @Estado 
	WHERE ID = @ID
END
GO

CREATE PROCEDURE SPActualizarVolcanEstado
	@ID INT,
	@Estado NVARCHAR(50)
AS
BEGIN
	UPDATE Volcan SET Estado = @Estado 
	WHERE ID = @ID
END
GO

-- Delete -> Eliminar un volcan por ID
CREATE PROCEDURE SPEliminarVolcan
@ID INT
AS
BEGIN
	DELETE FROM Volcan WHERE ID = @ID
END

insert into Volcan(Nombre,Ubicacion,Altura,Estado) values ('Volcán Arenal', 'Costa Rica',1700, 'Activo')


-- Nuevo

-- Crear tabla de historial de sensores
CREATE TABLE Historial_Sensor (
    ID INT PRIMARY KEY IDENTITY,
    Sensor_ID INT FOREIGN KEY REFERENCES Sensor(ID),
    Estado_Anterior NVARCHAR(50),
    Estado_Nuevo NVARCHAR(50),
    Fecha_Cambio DATETIME DEFAULT GETDATE(),
    Usuario NVARCHAR(100) DEFAULT SUSER_SNAME()
);
GO


-- Procedimiento almacenado para registrar un sismo
CREATE PROCEDURE SPRegistrarSismo
    @Volcan_ID INT,
    @Magnitud DECIMAL(3,1),
    @Profundidad INT
AS
BEGIN
    -- Verificar que la fecha no sea futura
    IF GETDATE() < GETDATE()
    BEGIN
        RAISERROR('No se pueden registrar sismos con fecha futura', 16, 1)
        RETURN
    END

    -- Registrar el sismo
    INSERT INTO Actividad_Sismica (Volcan_ID, Magnitud, Profundidad)
    VALUES (@Volcan_ID, @Magnitud, @Profundidad)

    -- Verificar si hay más de 5 sismos mayores a 4.5 en las últimas 12 horas
    DECLARE @SismosMayores INT

    SELECT @SismosMayores = COUNT(*)
    FROM Actividad_Sismica
    WHERE Volcan_ID = @Volcan_ID
        AND Magnitud > 4.5
        AND Fecha >= DATEADD(HOUR, -12, GETDATE())

    IF @SismosMayores >= 5
    BEGIN
        -- Cambiar estado del volcán a "Alerta"
        EXEC SPActualizarVolcanEstado @ID = @Volcan_ID, @Estado = 'Alerta'
    END
END
GO

-- Procedimiento almacenado para registrar una erupción
CREATE PROCEDURE SPRegistrarErupcion
    @Volcan_ID INT,
    @Tipo NVARCHAR(50),
    @Duracion_Horas INT
AS
BEGIN
    -- Verificar que no haya una erupción en el mismo día
    IF EXISTS(SELECT 1 FROM Erupcion WHERE Volcan_ID = @Volcan_ID 
              AND CONVERT(DATE, Fecha) = CONVERT(DATE, GETDATE()))
    BEGIN
        RAISERROR('Ya existe una erupción registrada para este volcán hoy', 16, 1)
        RETURN
    END

    -- Registrar la erupción
    INSERT INTO Erupcion (Volcan_ID, Tipo, Duracion_Horas)
    VALUES (@Volcan_ID, @Tipo, @Duracion_Horas)

    -- Verificar si el volcán ya estaba en alerta
    DECLARE @EstadoVolcan NVARCHAR(50)
    
    SELECT @EstadoVolcan = Estado
    FROM Volcan
    WHERE ID = @Volcan_ID

    IF @EstadoVolcan <> 'Erupción'
    BEGIN
        -- Cambiar estado del volcán a "Erupción"
        EXEC SPActualizarVolcanEstado @ID = @Volcan_ID, @Estado = 'Erupción'
    END

    -- Actualizar los sensores a "Falla"
    UPDATE Sensor
    SET Estado = 'Falla'
    WHERE Volcan_ID = @Volcan_ID
END
GO

-- Procedimiento almacenado para reparar un sensor
CREATE PROCEDURE SPRepararSensor
    @Sensor_ID INT
AS
BEGIN
    UPDATE Sensor
    SET Estado = 'Operativo'
    WHERE ID = @Sensor_ID
END
GO

-- Procedimientos para gestionar sensores
CREATE PROCEDURE SPInsertarSensor
    @Volcan_ID INT,
    @Ubicacion NVARCHAR(255)
AS
BEGIN
    INSERT INTO Sensor (Volcan_ID, Ubicacion, Estado)
    VALUES (@Volcan_ID, @Ubicacion, 'Operativo')
END
GO

CREATE PROCEDURE SPActualizarSensor
    @ID INT,
    @Ubicacion NVARCHAR(255),
    @Estado NVARCHAR(50)
AS
BEGIN
    UPDATE Sensor
    SET Ubicacion = @Ubicacion, Estado = @Estado
    WHERE ID = @ID
END
GO

CREATE PROCEDURE SPEliminarSensor
    @ID INT
AS
BEGIN
    DELETE FROM Historial_Sensor WHERE Sensor_ID = @ID
    DELETE FROM Sensor WHERE ID = @ID
END
GO

CREATE PROCEDURE SPObtenerSensoresVolcan
    @Volcan_ID INT
AS
BEGIN
    SELECT ID, Volcan_ID, Ubicacion, Estado
    FROM Sensor
    WHERE Volcan_ID = @Volcan_ID
END
GO

CREATE PROCEDURE SPObtenerSensorPorID
    @ID INT
AS
BEGIN
    SELECT ID, Volcan_ID, Ubicacion, Estado
    FROM Sensor
    WHERE ID = @ID
END
GO

-- Procedimientos para gestionar actividad sísmica
CREATE PROCEDURE SPObtenerSismosVolcan
    @Volcan_ID INT,
    @FechaInicio DATE = NULL,
    @FechaFin DATE = NULL
AS
BEGIN
    SELECT ID, Volcan_ID, Fecha, Magnitud, Profundidad
    FROM Actividad_Sismica
    WHERE Volcan_ID = @Volcan_ID
        AND (@FechaInicio IS NULL OR Fecha >= @FechaInicio)
        AND (@FechaFin IS NULL OR Fecha <= @FechaFin)
    ORDER BY Fecha DESC
END
GO

CREATE PROCEDURE SPObtenerSismoPorID
    @ID INT
AS
BEGIN
    SELECT ID, Volcan_ID, Fecha, Magnitud, Profundidad
    FROM Actividad_Sismica
    WHERE ID = @ID
END
GO

-- Procedimientos para gestionar erupciones
CREATE PROCEDURE SPObtenerErupcionesVolcan
    @Volcan_ID INT
AS
BEGIN
    SELECT ID, Volcan_ID, Fecha, Tipo, Duracion_Horas
    FROM Erupcion
    WHERE Volcan_ID = @Volcan_ID
    ORDER BY Fecha DESC
END
GO

CREATE PROCEDURE SPObtenerErupcionPorID
    @ID INT
AS
BEGIN
    SELECT ID, Volcan_ID, Fecha, Tipo, Duracion_Horas
    FROM Erupcion
    WHERE ID = @ID
END
GO

-- Triggers

-- Trigger para evitar duplicidad de erupciones
CREATE TRIGGER TR_Erupcion_EvitarDuplicidad
ON Erupcion
INSTEAD OF INSERT
AS
BEGIN
    DECLARE @Volcan_ID INT
    DECLARE @Tipo NVARCHAR(50)
    DECLARE @Duracion_Horas INT

    SELECT @Volcan_ID = Volcan_ID, @Tipo = Tipo, @Duracion_Horas = Duracion_Horas
    FROM inserted

    -- Verificar que no haya una erupción en el mismo día
    IF EXISTS(SELECT 1 FROM Erupcion WHERE Volcan_ID = @Volcan_ID 
              AND CONVERT(DATE, Fecha) = CONVERT(DATE, GETDATE()))
    BEGIN
        RAISERROR('Ya existe una erupción registrada para este volcán hoy', 16, 1)
        RETURN
    END

    -- Si no hay duplicidad, insertar el registro
    INSERT INTO Erupcion (Volcan_ID, Tipo, Duracion_Horas)
    VALUES (@Volcan_ID, @Tipo, @Duracion_Horas)
END
GO

-- Trigger para marcar sensores en "Falla" durante erupciones
CREATE TRIGGER TR_Erupcion_MarcarSensores
ON Erupcion
AFTER INSERT
AS
BEGIN
    DECLARE @Volcan_ID INT
    
    SELECT @Volcan_ID = Volcan_ID
    FROM inserted

    -- Marcar todos los sensores del volcán como "Falla"
    UPDATE Sensor
    SET Estado = 'Falla'
    WHERE Volcan_ID = @Volcan_ID
END
GO

-- Trigger para controlar eliminaciones peligrosas
CREATE TRIGGER TR_Volcan_ControlarEliminacion
ON Volcan
INSTEAD OF DELETE
AS
BEGIN
    DECLARE @Volcan_ID INT
    
    SELECT @Volcan_ID = ID
    FROM deleted

    -- Verificar si hay sismos o erupciones registradas
    IF EXISTS(SELECT 1 FROM Actividad_Sismica WHERE Volcan_ID = @Volcan_ID)
        OR EXISTS(SELECT 1 FROM Erupcion WHERE Volcan_ID = @Volcan_ID)
    BEGIN
        RAISERROR('No se puede eliminar un volcán que tiene sismos o erupciones registradas', 16, 1)
        RETURN
    END

    -- Si no hay registros asociados, eliminar sensores primero (integridad referencial)
    DELETE FROM Sensor WHERE Volcan_ID = @Volcan_ID
    
    -- Luego eliminar el volcán
    DELETE FROM Volcan WHERE ID = @Volcan_ID
END
GO

-- Trigger para auditar cambios de estado en sensores
CREATE TRIGGER TR_Sensor_AuditarCambios
ON Sensor
AFTER UPDATE
AS
BEGIN
    IF UPDATE(Estado)
    BEGIN
        INSERT INTO Historial_Sensor (Sensor_ID, Estado_Anterior, Estado_Nuevo)
        SELECT i.ID, d.Estado, i.Estado
        FROM inserted i
        INNER JOIN deleted d ON i.ID = d.ID
        WHERE i.Estado <> d.Estado
    END
END
GO

-- Función para calcular el promedio de magnitud de sismos de un volcán
CREATE FUNCTION FN_PromedioMagnitudSismos
(
    @Volcan_ID INT
)
RETURNS DECIMAL(3,1)
AS
BEGIN
    DECLARE @Promedio DECIMAL(3,1)
    
    SELECT @Promedio = AVG(Magnitud)
    FROM Actividad_Sismica
    WHERE Volcan_ID = @Volcan_ID
    
    RETURN ISNULL(@Promedio, 0)
END
GO

-- Función para obtener el número de sensores operativos de un volcán
CREATE FUNCTION FN_SensoresOperativos
(
    @Volcan_ID INT
)
RETURNS INT
AS
BEGIN
    DECLARE @Contador INT
    
    SELECT @Contador = COUNT(*)
    FROM Sensor
    WHERE Volcan_ID = @Volcan_ID AND Estado = 'Operativo'
    
    RETURN ISNULL(@Contador, 0)
END
GO

-- Procedimiento para obtener estadísticas de un volcán
CREATE PROCEDURE SPObtenerEstadisticasVolcan
    @Volcan_ID INT
AS
BEGIN
    DECLARE @NombreVolcan NVARCHAR(100)
    DECLARE @EstadoVolcan NVARCHAR(50)
    DECLARE @TotalSismos INT
    DECLARE @PromedioMagnitud DECIMAL(3,1)
    DECLARE @TotalErupciones INT
    DECLARE @SensoresOperativos INT
    DECLARE @SensoresFalla INT
    
    -- Obtener información del volcán
    SELECT @NombreVolcan = Nombre, @EstadoVolcan = Estado
    FROM Volcan
    WHERE ID = @Volcan_ID
    
    -- Contar sismos
    SELECT @TotalSismos = COUNT(*)
    FROM Actividad_Sismica
    WHERE Volcan_ID = @Volcan_ID
    
    -- Obtener promedio de magnitud
    SET @PromedioMagnitud = dbo.FN_PromedioMagnitudSismos(@Volcan_ID)
    
    -- Contar erupciones
    SELECT @TotalErupciones = COUNT(*)
    FROM Erupcion
    WHERE Volcan_ID = @Volcan_ID
    
    -- Contar sensores operativos
    SELECT @SensoresOperativos = COUNT(*)
    FROM Sensor
    WHERE Volcan_ID = @Volcan_ID AND Estado = 'Operativo'
    
    -- Contar sensores en falla
    SELECT @SensoresFalla = COUNT(*)
    FROM Sensor
    WHERE Volcan_ID = @Volcan_ID AND Estado = 'Falla'
    
    -- Devolver estadísticas
    SELECT 
        @NombreVolcan AS NombreVolcan,
        @EstadoVolcan AS EstadoVolcan,
        @TotalSismos AS TotalSismos,
        @PromedioMagnitud AS PromedioMagnitud,
        @TotalErupciones AS TotalErupciones,
        @SensoresOperativos AS SensoresOperativos,
        @SensoresFalla AS SensoresFalla
END
GO