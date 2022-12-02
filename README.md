
# Sesión Practica - Equipo #4 - Patrón de Diseño Factory

[**Clic aquí para ir directamente al ejercicio de la sesión practica**](#ejercicio-sesion-practica)

## Elementos del Repositorio

### - Diagrama de Clases

![WhatsApp Image 2022-12-01 at 23 30 27](https://user-images.githubusercontent.com/89323622/205222188-9cb2a9be-b4c7-4478-8227-ee596fb3ba6e.jpeg)


### - Paquetería `abrahamxts`
Paquete principal del proyecto en donde se encuentra la clase ejecutable y las clases base para crear el Factory.

### - Paquetería `abrahamxts.dao`
En este paquete se encuentra nuestra clase de acceso a datos ProductDAO la cual utilizará el Factory para obtener las conexiones a la base de datos.

### - Paquetería `abrahamxts.models`
Contiene las clases de entidad para persistir los productos.

### - Paquetería `abrahamxts.adapters`
Contiene las clases de las conexiones a las bases de datos.

### - Archivo `DBFactory.properties`
Archivo que define las propiedades para la clase `DBFactory`, en este caso define una conexión default en PostgreSQL.
```
DB_CLASS com.abrahamxts.adapters.PostgreSQLDBAdapter
#defaultDBClass com.abrahamxts.adapters.MySQLDBAdapter
```

### - Archivo `DBMySQL.properties`
Archivo con las credenciales necesarias para generar la conexión a la base de datos MySQL.
```
HOST abrahamxtsserver.mysql.database.azure.com
PORT 3306
DATABASE pruebas
USER abrahamxts@abrahamxtsserver
PASSWORD Abraham150416
```

### - Archivo `DBPostgreSQL.properties`
Archivo con las credenciales necesarias para generar la conexión a la base de datos PostgreSQL.
```
HOST postgrexts.postgres.database.azure.com
PORT 5432
DATABASE pruebas
USER abrahamxts@postgrexts
PASSWORD Abraham150416
```


### - Interfaz `IDBAdapter`
Interfaz que define la estructura de los productos que podrá crear el Factory, en este caso habrá dos clases concretas, una para MySQL y otra para PostgreSQL las cuales veremos más adelante.
```java
package com.abrahamxts;
import java.sql.Connection;
public interface IDBAdapter {
	public Connection getConnection();
}
```
### - Clase `PostgreSQLDBAdapter`
Clase que genera la conexión con la base de datos en `PostgreSQL`. La clase lee del archivo `DBPostgreSQL.properties` para generar la conexión. Implementa la interfaz `IDBAdapter`.

```java
package com.abrahamxts.adapters;

import java.sql.*;
import java.util.Properties;

import com.abrahamxts.IDBAdapter;
import com.abrahamxts.util.PropertiesUtil;

public class PostgreSQLDBAdapter implements IDBAdapter {

    private static final String DB_PROPERTIES = "META-INF/DBPostgreSQL.properties";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {

        Properties properties = PropertiesUtil.loadProperty(DB_PROPERTIES);

		String user = properties.getProperty("USER");
        String password = properties.getProperty("PASSWORD");

        try {
            String connectionString = createConnectionString();
            Connection connection = DriverManager.getConnection(connectionString, user, password);

            System.out.println("Clase de conexión ==> " + connection.getClass().getName());

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createConnectionString() {
        Properties properties = PropertiesUtil.loadProperty(DB_PROPERTIES);
		
        String host = properties.getProperty("HOST");
        String port = properties.getProperty("PORT");
        String db = properties.getProperty("DATABASE");

        String connectionString = "jdbc:postgresql://" + host + ":" + port + "/" + db;
        
		System.out.println("String de conexión ==> " + connectionString);
        
		return connectionString;
    }
}
```

### - Clase `MySQLDBAdapter`
Clase que genera la conexión con la base de datos en `MySQL`. La clase lee del archivo `DBMySQL.properties` para generar la conexión. Implementa la interfaz `IDBAdapter`.

```java
package com.abrahamxts.adapters;

import java.sql.*;
import java.util.Properties;

import com.abrahamxts.IDBAdapter;
import com.abrahamxts.util.PropertiesUtil;

public class MySQLDBAdapter implements IDBAdapter {

    private static final String DB_PROPERTIES = "META-INF/DBMySQL.properties";

    static {
        try {
            new com.mysql.jdbc.Driver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        try {
            String connectionString = createConnectionString();
            Connection connection = DriverManager.getConnection(connectionString);

            System.out.println("Clase de conexión ==> " + connection.getClass().getName());

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createConnectionString() {
        Properties properties = PropertiesUtil.loadProperty(DB_PROPERTIES);
		
        String host = properties.getProperty("HOST");
        String port = properties.getProperty("PORT");
        String db = properties.getProperty("DATABASE");
        String user = properties.getProperty("USER");
        String password = properties.getProperty("PASSWORD");

        String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + password;
        
		System.out.println("String de conexión ==> " + connectionString);
        
		return connectionString;
    }
}
```


### -Clase `DBFactory`

Métodos:
 -  `getDBAdapter(dbType)`
 Método que nos permite generar explícitamente que tipo de conexión por medio del parámetro `dbType`, que es un elemento en la Enumeración `DBType`. Para este ejemplo se puede elegir entre `MySQL` y `PostgreSQL`.
 
 - `getDefaultDBAdapter()`
 Este método retorna la conexión por default que tiene la Factory, esto definido por el archivo `DBFactory.properties`.
 

### - Enum `DBType`
Enumerados en donde se encuentran nuestras opciones de conexión a bases de datos.
```java
package com.abrahamxts;

public enum DBType {
	MySQL, Oracle,
}
```


### - Clase `Product`
Clase que representara los registros de esta sesión práctica. 

```java
package com.abrahamxts.models;

public class Product {

    private Long idProduct;

    private String productName;

    private double price;

    public Product(Long idProduct, String productName, double price) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.price = price;
    }

    public Product() {}

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product {" + "idProduct = " + idProduct + ", productName = " + productName + ", price = " + price + '}';
    }
}
```

### - Clase `ProductDAO`
Clase DAO (Data Access Object) que lee y escribe en la base de datos de los productos.

```java
package com.abrahamxts.DAO;

import java.sql.*;
import java.util.*;

import com.abrahamxts.DBFactory;
import com.abrahamxts.IDBAdapter;
import com.abrahamxts.models.Product;

public class ProductDAO {
    
    private IDBAdapter dbAdapter;
    
    public ProductDAO() {
        dbAdapter = DBFactory.getDefaultDBAdapter();
    }
    
    public List<Product> findAllProducts() {

        Connection connection = dbAdapter.getConnection();

        List<Product> productList = new ArrayList<>();

        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT idProductos, productName, productPrice FROM Productos");

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                productList.add(new Product(results.getLong(1), 
                    results.getString(2), 
					results.getDouble(3)
				));
            }

            return productList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (Exception e) {}
        }
    }
    
    public boolean saveProduct(Product product){
        Connection connection = dbAdapter.getConnection();

        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO Productos(idProductos, productName, productPrice) VALUES (?,?,?)");

            statement.setLong(1, product.getIdProduct());
            statement.setString(2, product.getProductName());
            statement.setDouble(3, product.getPrice());
            statement.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (Exception e) {}
        }
    }
}
```

### - Clase `PropertiesUtil`
Clase de utilería que nos permite leer los archivos `.properties` para los adaptadores y la clase `Factory`.

```java
package com.abrahamxts.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties loadProperty(String propertiesURL) {
        try {
            Properties properties = new Properties();
            InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesURL);

            properties.load(inputStream);
			
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

## Ejercicio Sesion Practica

[**Resultado sesión practica**](#resultado)


**ANTES DE EMPEZAR:** Es importante clonar la branch `demo` de este repositorio. Dentro de un directorio en su computadora y en la terminal ejecuten el siguiente comando:

```git
git clone https://github.com/AbrahamXTS/FactoryPattern --branch demo
```

Con esto habremos clonado los archivos en la branch `demo`.

Posterior a esto cambiamos la version del maven a la de nuestro jdk local, para esto nos ubicamos en el archivo `pom.xml` y en la etiqueta de `properties`

```xml
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<maven.compiler.source>17</maven.compiler.source>
	<maven.compiler.target>17</maven.compiler.target>
</properties>
```
y cambiamos en las lineas `<maven.compiler.source>17</maven.compiler.source>` y `<maven.compiler.target>17</maven.compiler.target>` el valor de 17 al valor de su version de jdk.

Para consultar su version de jdk les dejamos un tutorial [aqui](https://www.java.com/es/download/help/version_manual.html).

### Paso #1 - `DBFactory.java` y `getDBAdapter`

Dentro de la carpeta `abrahamxts` accedemos a la clase `DBFactory.java`. Si abrimos el archivo nos encontraremos con lo siguiente:

```java
package com.abrahamxts;

import java.util.Properties;
import com.abrahamxts.util.*;
import com.abrahamxts.adapters.*;

public class DBFactory {

    public static IDBAdapter getDBadapter(DBType dbType) {
		// Escribe tu código aquí
    }

    public static IDBAdapter getDefaultDBAdapter() {
		// Escribe tu código aquí
    }
}
```

Entonces, lo primero que tendremos que hacer es implementar el método `getDBAdapter()`.
Dentro de este método implementaremos una estructura switch donde cubriremos cada opción posible de conexión a la base de datos.

```java
switch (dbType) {
  case MySQL:
    return new MySQLDBAdapter();
   case PostgreSQL:
    return new PostgreSQLDBAdapter();
   default:
    throw new IllegalArgumentException("Base de datos no soportada.");
}
```
En esta estructura estamos devolviendo una conexión según la base de datos solicitada, y en caso de que no sea ninguna de las soportadas, retorna como valor predetermina una Excepción del tipo `IllegalArgumentException` 

### Método `getDefaultDBAdapter()`

Este método está pensado para cuando no se solicita una conexión en específico, entonces se retorna la base de datos por defecto.

Primero se crea una instancia de la clase de utilería `Properties`, que es la que nos ayudara a leer las propiedades de la clase `DBFactory`, establecidas en el archivo `DBFactory.properties` y en las cuales esta puesta una clase de una conexión por defecto.

```java
Properties properties = PropertiesUtil.loadProperty("META-INF/DBFactory.properties");
```

Una vez hecho esto, crearemos un Sting que imprimiremos en la consola a manera de "debug" de ver cuál es la base de datos predeterminada.

```java
String defaultDBClass = properties.getProperty("DB_CLASS");
System.out.println("Clase de Base de datos por defecto ==> " + defaultDBClass);
```

Luego retornamos una instancia de esta conexión por defecto:

```java
  return (IDBAdapter) Class.forName(defaultDBClass).getDeclaredConstructor().newInstance();
```

Es importante que todo este procedimiento vaya dentro de un bloque Try - catch ya que puede ocurrir un error al momento de intentar leer el archivo `.properties`

Entonces así resultaría el método al final:

```java
public static IDBAdapter getDefaultDBAdapter() {
    try {
        Properties properties = PropertiesUtil.loadProperty("META-INF/DBFactory.properties");
        String defaultDBClass = properties.getProperty("DB_CLASS");
        System.out.println("Clase de datos por defecto ==> " + defaultDBClass);
	
        return (IDBAdapter) Class.forName(defaultDBClass).getDeclaredConstructor().newInstance();
    } catch (Exception e) {
	System.out.println("No se ha podido acceder al archivo de propiedades: " + e.getMessage());
        return null;
    }
```


Una vez listo, no queda mas que guardar y ejecutar la clase `App.java` con el codigo ya existente o modificarlo a su gusto.




## Resultado

### `DBFactory.java`

```java
package com.abrahamxts;

import java.util.Properties;
import com.abrahamxts.util.*;
import com.abrahamxts.adapters.*;

public class DBFactory {

    private static final String DB_FACTORY_PROPERTY_URL = "META-INF/DBFactory.properties";

    public static IDBAdapter getDBadapter(DBType dbType) {
        switch (dbType) {
            case MySQL:
                return new MySQLDBAdapter();
            case PostgreSQL:
                return new PostgreSQLDBAdapter();
            default:
                throw new IllegalArgumentException("Base de datos no soportada.");
        }
    }

    public static IDBAdapter getDefaultDBAdapter() {
        try {
            Properties properties = PropertiesUtil.loadProperty(DB_FACTORY_PROPERTY_URL);

            String defaultDBClass = properties.getProperty("DB_CLASS");

            System.out.println("Clase de datos por defecto ==> " + defaultDBClass);
			
            return (IDBAdapter) Class.forName(defaultDBClass).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

### `App.java`
```java
package com.abrahamxts;

import java.util.List;
import java.sql.SQLException;

import com.abrahamxts.DAO.ProductDAO;
import com.abrahamxts.models.Product;

public class App {

    public static void main(String[] args) throws SQLException {
	
        Product productA = new Product(1F, "Producto A", 100F);
        Product productB = new Product(2F, "Producto B", 100F);
        
        ProductDAO productDAO = new ProductDAO();
        
        productDAO.saveProduct(productA);
        productDAO.saveProduct(productB);
        
        List<Product> products = productDAO.findAllProducts();

        System.out.println("Total de productos ==> " + products.size());
		
        for (Product product : products){
            System.out.println(product);
        }
    }
}
```



