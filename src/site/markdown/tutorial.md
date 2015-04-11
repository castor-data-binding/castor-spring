# Spring ORM for (experienced) Castor JDO users - A possible migration

A migration guide for experienced Castor JDO users
  
This guide assumes that you are an experienced Castor JDO users that knows how
to use Castor's interfaces and classes to interact with a database. If 
this is not the case, please familiarize yourself with 
<a href="http://www.castor.org/jdo.html">Castor JDO first</a>.  

## Sample domain objects
	
The sample domain objects used in here basically define a <tt>Catalogue</tt>, 
which is a collection of <tt>Product</tt>s. A possible castor JDO mapping could look
as follows:
	
```xml
<class name="org.castor.sample.Catalogue">
   <map-to table="catalogue"/>
   <field name="id" type="long">
      <sql name="id" type="integer" />
   </field>
   <field name="products" type="org.castor.sample.Product" collection="arraylist">
      <sql many-key="c_id" />
   </field>
</class>

<class name="org.castor.sample.Product">
   <map-to table="product"/>
   <field name="id" type="long">
      <sql name="id" type="integer" />
   </field>
   <field name="description" type="string">
      <sql name="desc" type="varchar" />
   </field>
</class>
```
		
## Using Castor JDO manually
	
To e.g. load a given <tt>Catalogue</tt> instance as defined by its identity,
and all its associated <tt>Product</tt> instances, the following code could be used, 
based upon the Castor-specific interfaces <tt>JDOManager</tt> and <tt>Database</tt>.
	
```java
JDOManager.loadConfiguration("jdo-conf.xml");
JDOManager jdoManager = JDOmanager.createInstance("sample");

Database database = jdoManager.getDatabase();		
database.begin();
Catalogue catalogue = database.load(catalogue.class, new Long(1));
database.commit();
database.close();
```
		
For brevity, exception handling has been ommitted completely. But is is quite obvious
that - when using such code fragments to implement various methods of a DAO - 
there's a lot of redundant code that needed to be written again and again - and exception
handling is adding some additional complexity here as well.
	
Enters Spring ORM for Castor JDO, a small layer that allows usage of Castor JDO
through Spring ORM, with all the known benefits (exception conversion, templates, tx handling).
		
## Using Castor JDO with Spring ORM - Without CastorTemplate
	
Let's see how one might implement the <tt>loadProduct(int)</tt> of a <tt>ProductDAO</tt>
class with the help of Spring ORM using Castor JDO:
	
```
public class ProductDaoImpl implements ProductDao {

  private JDOManager jdoManager;

  public void setJDOManager(JDOManager jdoManager) {
    this.jdoManager = jdoManager;
  }

  public Product loadProduct(final int id) {
    CastorTemplate tempate = new CastorTemplate(this.jdoManager);
    return (Product) template.execute(
        new CastorCallback() {
          public Object doInJdo(Database database) throws PersistenceException {
            return (Product) database.load(Product.class, new Integer (id));
          }
        });
  }
}
```

Still a lot of code to write, but compared to the above section, the DAO gets
passed a fully configured <tt>JDOManager</tt> instance through Spring's dependency
injection mechanism. All that's required is configuration of Castor's JDOManager
as a Spring bean definition in an Spring application context as 
follows.
	
```
<bean id="jdoManager" class="org.castor.spring.orm.LocalCastorFactoryBean">
  <property name="databaseName" value="test" />
  <property name="configLocation" value="classpath:jdo-conf.xml" />
</bean>

<bean id="myProductDao" class="product.ProductDaoImpl">
  <property name="JDOManager">
    <ref bean="jdoManager"/>
  </property>
</bean>
```

## Using Castor JDO with Spring ORM - With CastorTemplate
	
Above code is still quite verbous, as it requires you to write short (though complex)
callback functions. To ease life of the Castor JDO user even more, a range of template 
methods have been added to <tt>CastorTemplate</tt>, allowing the implementation of above
<tt>ProductDAO</tt> to be shortened considerably.
	
```
public class ProductDaoImplUsingTemplate extends CastorTemplate implements ProductDao {

  private JDOManager jdoManager;

  public void setJDOManager(JDOManager jdoManager) {
    this.jdoManager = jdoManager;
  }

  public Product loadProduct(final int id) {
    return (Product) load(Integer.valueOf(id));
  }
  
  ...
}
```

Changing the bean definition for <tt>myProductDAO</tt> to ...
		
```
<bean id="myProductDao" class="product.ProductDaoImplUsingTemplate">
  <property name="JDOManager">
    <ref bean="myJdoManager"/>
  </property>
</bean>
```
		
loading an instance of <tt>Product</tt> by its identifier is reduced to ...
		
```
ProductDao dao = (ProductDAO) context.getBean ("myProductDAO");
Product product = dao.load(1);
```

## Using Castor JDO with Spring ORM - With CastorDaoSupport
	
Alternatively to extending <tt>CastorTemplate</tt>, one could extend the
<tt>CastorDaoSupport</tt> class and implement the <tt>ProductDAO</tt> as 
follows.
	
```
public class ProductDaoImplUsingDaoSupport extends CastorDaoSupport implements ProductDao {

  private JDOManager jdoManager;

  public void setJDOManager(JDOManager jdoManager) {
    this.jdoManager = jdoManager;
  }

  public Product loadProduct(final int id) {
    return (Product) getCastorTemplate().load(Integer.valueOf(id));
  }
  
  ...
}
```

Changing the bean definition for <tt>myProductDAO</tt> to ...
		
```
<bean id="myProductDao" class="product.ProductDaoImplUsingDaoSupport">
  <property name="JDOManager">
    <ref bean="myJdoManager"/>
  </property>
</bean>
```

		
the code to load an instance of <tt>Product</tt> still is as shown above.
		
## Complete Spring configuration (using Template)

```
<bean id="jdoManager" class="org.castor.spring.orm.LocalCastorFactoryBean">
  <property name="databaseName" value="test" />
  <property name="configLocation" value="classpath:jdo-conf.xml" />
</bean>

<bean id="myProductDao" class="product.ProductDaoImplUsingTemplate">
  <property name="JDOManager">
    <ref bean="myJdoManager"/>
  </property>
</bean>
```
