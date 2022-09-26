## Association mappings 

- Referenece: https://thorben-janssen.com/ultimate-guide-association-mappings-jpa-hibernate/
- One of the key features of JPA and Hibernate. 
- They model the relationship between two database tables as attributes in your domain model. 
- That allows you to easily navigate the associations in your domain model and JPQL or Criteria queries.
- JPA and Hibernate support the same associations as you know from your relational database model. You can use:
    - one-to-one associations
    - many-to-one associations 
    - many-to-many associations
- You can map each of them as a uni- or bi-directional association. 
    - That means you can either model them as an attribute on only one of the associated entities or on both. 
    - That has no impact on your database mapping, but it defines in which direction you can use the relationship in your domain model and JPQL or Criteria queries.  

## Many-to-One Associations - eg. Order and OrderItem

- An order consists of multiple items, but each item belongs to only one order. 
- If you want to model this in your database model, you need to store the primary key of the Order record as a foreign key in the OrderItem table.
- With JPA and Hibernate, you can model this in 3 different ways:
    - You can either model it as a bi-directional association with an attribute on the Order and the OrderItem entity. 
    - Or you can model it as a uni-directional relationship with an attribute on the Order or the OrderItem entity.

### Uni-directional Many-to-One Association - eg. from OrderItem to Order

- The uni-directional mapping on the OrderItem entity (ie. the many side)
- The OrderItem table contains the foreign key of the record in the Order table.
- You can model this association with an attribute of type Order and a @ManyToOne annotation. 

```
@Entity
public class OrderItem {
    @ManyToOne
    private Order order;
}
```
- By default, Hibernate generates the name of the foreign key column **based on the name of the relationship mapping attribute and the name of the primary key attribute**. In this example, Hibernate would use a column with the name **order_id** to store the foreign key to the Order entity.
- If you want to use a different column, you need to define the foreign key column name with a `@JoinColumn` annotation. For below code,  the column `fk_order` is used to store the foreign key.

```
@Entity
public class OrderItem {
    @ManyToOne
    @JoinColumn(name = "fk_order")
    private Order order;
}
```

- You can get the Order for a given OrderItem and to add or remove an OrderItem to or from an existing Order.

```
Order o = em.find(Order.class, 1L);
OrderItem i = new OrderItem();
i.setOrder(o);
em.persist(i);
```

### Uni-directional One-to-Many Association - eg. from Order to OrderItem

- The uni-directional one-to-many relationship mapping is not very common. 
- The basic mapping definition is very similar to the many-to-one association. It consist of the `List` items attribute which stores the associated entities and a @OneToMany association.

```
@Entity
public class Order {
    @OneToMany //will create an 'association table', not a FK column
    private List<OrderItem> items = new ArrayList<OrderItem>();
}
```

- But the above code will let Hibernate use an `association table` to map the relationship. If you want to avoid that, **you need to use a `@JoinColumn` annotation to specify the foreign key column.**
- The `@JoinColumn` annotation tells Hibernate to use the `fk_order` column in the OrderItem table to join the two database tables.

```
@Entity
public class Order {
    @OneToMany
    @JoinColumn(name = "fk_order")
    private List<OrderItem> items = new ArrayList<OrderItem>();
}
```

- You can get all OrderItems of an Order and to add or remove an OrderItem to or from an Order.

```
Order o = em.find(Order.class, 1L);
OrderItem i = new OrderItem();
o.getItems().add(i);
em.persist(i);
```

### Bi-directional Many-to-One Association

- The bi-directional Many-to-One association mapping is the most common way to model this relationship with JPA and Hibernate. 
- It uses an attribute on the Order and the OrderItem entity. This allows you to navigate the association in both directions in your domain model and your JPQL queries.
- The mapping definition consists of 2 parts:
    - The many side (also the owning side) : which **owns the relationship mapping** 
    - The one side (also the referencing side): which **references the mapping**

- The owning side: It is the same as the uni-directional Many-to-One association mapping. It consists of the Order order attribute, a @ManyToOne annotation and an optional @JoinColumn annotation.

```
@Entity
public class OrderItem {
    @ManyToOne
    @JoinColumn(name = "fk_order")
    private Order order;
}
```

- The referencing side: you just need to reference the owning association mapping. You can do that by providing the name of the association-mapping attribute to the `mappedBy` attribute of the @OneToMany annotation.

```
@Entity
public class Order {
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<OrderItem>();
}
```

- You can now use this association in a similar way as the unidirectional relationships, but **adding and removing an entity from the relationship requires an additional step, ie you need to update both sides of the association.**
    - That is an error-prone task, and a lot of developers prefer to implement it in a utility method which updates both entities.

```
Order o = em.find(Order.class, 1L);
OrderItem i = new OrderItem();
i.setOrder(o);
o.getItems().add(i);
em.persist(i);
```

```
@Entity
public class Order { 
    //an utility method to update both sides of the association
    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }
}
```

## Many-to-Many Associations - eg. Stores and Products

- On the database level, it requires an additional `association table` which contains the primary key pairs of the associated entities. But **you don’t need to map this table to an entity.**
- A typical example for such a many-to-many association are Products and Stores. Each Store sells multiple Products and each Product gets sold in multiple Stores.
- You can model a many-to-many relationship as a uni- or bi-directional relationship between two entities.
- But there is an important difference that might not be obvious when you look at the following code snippets. **When you map a many-to-many association, you should use a `Set` instead of a List as the attribute type.** Otherwise, Hibernate will take a very inefficient approach to remove entities from the association. **It will remove all records from the association table and re-insert the remaining ones. You can avoid that by using a Set instead of a List as the attribute type.**

### Uni-directional Many-to-Many Associations

- Similar to the previously discussed mappings, the uni-directional many-to-many relationship mapping requires an entity attribute and a @ManyToMany annotation. 

```
@Entity
public class Store {
    @ManyToMany
    private Set<Product> products = new HashSet<Product>(); //note the use of 'Set'
}
```

- If you don’t provide any additional information, Hibernate uses its default mapping which expects **an association table with the name of both entities** and the primary key attributes of both entities. 
    - In this case, Hibernate uses the **Store_Product** table with the columns **store_id** and **product_id**.
- You can customize that with a `@JoinTable annotation` and its attributes `joinColumns` and `inverseJoinColumns`. 
    - The `joinColumns` attribute defines the foreign key columns for the entity on which you define the association mapping. 
    - The `inverseJoinColumns` attribute specifies the foreign key columns of the associated entity.

- Below shows a mapping that tells Hibernate to use the `store_product` table with the `fk_product` column as the foreign key to the Product table and the `fk_store` column as the foreign key to the Store table.

```
@Entity
public class Store {
    @ManyToMany
    @JoinTable(name = "store_product",
           joinColumns = { @JoinColumn(name = "fk_store") },
           inverseJoinColumns = { @JoinColumn(name = "fk_product") })
    private Set<Product> products = new HashSet<Product>();
}
```
- That’s all you have to do to define an uni-directional many-to-many association between two entities.

```
Store s = em.find(Store.class, 1L);
Product p = new Product();
s.getProducts().add(p);
em.persist(p);
```

### Bi-directional Many-to-Many Associations

- The mapping follows the same concept as the bi-directional mapping of a many-to-one relationship.
- The owning side (choose one, here Store is used as the owning side): The mapping is identical to the uni-directional many-to-many association mapping.

```
@Entity
public class Store {
    @ManyToMany
    @JoinTable(name = "store_product",
        joinColumns = { @JoinColumn(name = "fk_store") },
        inverseJoinColumns = { @JoinColumn(name = "fk_product") })
    private Set<Product> products = new HashSet<Product>(); 
}
```

- The referencing side: Similar to the bi-directional many-to-one relationship mapping, you just need to reference the attribute that owns the association.
- The Set<Product> products attribute of the Store entity owns the association. So, you only need to provide the String “products” to the `mappedBy` attribute of the @ManyToMany annotation.

```
@Entity
public class Product{
    @ManyToMany(mappedBy="products")
    private Set<Store> stores = new HashSet<Store>();
}
```

- You need to **update both ends of a bidirectional association when you want to add or remove an entity.** 
- Doing that in your business code is verbose and error-prone. It’s, therefore, a good practice to provide helper methods which update the associated entities.

```
@Entity
public class Store {
    // an utility method to add the associated entities
    public void addProduct(Product p) {
        this.products.add(p);
        p.getStores().add(this);
    }
 
    public void removeProduct(Product p) {
        this.products.remove(p);
        p.getStores().remove(this);
    }
 
    …
}
```

## One-to-One Associations  - eg. Customer and ShippingAddress

- Each Customer has exactly one ShippingAddress and each ShippingAddress belongs to one Customer. On the database level, this mapped by a foreign key column either on the ShippingAddress or the Customer table.

### Uni-directional One-to-One Associations - eg. From Customer to ShippingAddress

```
@Entity
public class Customer{
    @OneToOne
    @JoinColumn(name = "fk_shippingaddress")
    private ShippingAddress shippingAddress;
}
```

```
Customer c = em.find(Customer.class, 1L);
ShippingAddress sa = c.getShippingAddress();
```

### Bi-directional One-to-One Associations

- The bi-directional one-to-one relationship consists of an owning and a referencing side. **The owning side of the association defines the mapping, and the referencing one just links to that mapping.**

- The owning side of the association

```
@Entity
public class Customer{
    @OneToOne
    @JoinColumn(name = "fk_shippingaddress")
    private ShippingAddress shippingAddress;
}
```

- The referencing side of the association

```
@Entity
public class ShippingAddress{
    @OneToOne(mappedBy = "shippingAddress")
    private Customer customer;
}
```