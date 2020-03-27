# Demo Data Dictionary
*generated: Fri Mar 27 10:06:43 PDT 2020*

## Document Index

- <a href="#additional_charge">additional_charge</a>
- <a href="#customer">customer</a>
- <a href="#line_item">line_item</a>
- <a href="#manufacturer">manufacturer</a>
- <a href="#order">order</a>
- <a href="#person">person</a>
- <a href="#product">product</a>

## Field Index

- <a href="#active">active</a>
- <a href="#address">address</a>
- <a href="#address2">address2</a>
- <a href="#amount">amount</a>
- <a href="#cell_phone">cell_phone</a>
- <a href="#charge">charge</a>
- <a href="#charges">charges</a>
- <a href="#city">city</a>
- <a href="#contact_email">contact_email</a>
- <a href="#count">count</a>
- <a href="#country">country</a>
- <a href="#customer_id">customer_id</a>
- <a href="#delivery_fee">delivery_fee</a>
- <a href="#email">email</a>
- <a href="#fee">fee</a>
- <a href="#fees">fees</a>
- <a href="#first_name">first_name</a>
- <a href="#home_phone">home_phone</a>
- <a href="#id">id</a>
- <a href="#ids">ids</a>
- <a href="#label">label</a>
- <a href="#last_name">last_name</a>
- <a href="#latitude">latitude</a>
- <a href="#line_items">line_items</a>
- <a href="#long_lat">long_lat</a>
- <a href="#long_lats">long_lats</a>
- <a href="#longitude">longitude</a>
- <a href="#manufacturer_id">manufacturer_id</a>
- <a href="#name">name</a>
- <a href="#notes">notes</a>
- <a href="#notification_email">notification_email</a>
- <a href="#phone">phone</a>
- <a href="#postal_code">postal_code</a>
- <a href="#product_id">product_id</a>
- <a href="#province">province</a>
- <a href="#sales_tax">sales_tax</a>
- <a href="#sku">sku</a>
- <a href="#subtotal">subtotal</a>
- <a href="#tag">tag</a>
- <a href="#tags">tags</a>
- <a href="#tax">tax</a>
- <a href="#taxes">taxes</a>
- <a href="#total">total</a>
- <a href="#unit_cost">unit_cost</a>
- <a href="#work_phone">work_phone</a>


## Documents

<a name="additional_charge"></a>

### Additional Charge

- **ID**: <a href="#additional_charge">additional_charge</a>
- **Optional (Unqualified)**: [ ]
- **Required (Unqualified)**: [ ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{ }
```
- **EDN Example**: (required and optional field(s))

```clojure
{}

```

<a name="customer"></a>

### Customer

- **Meta**: 
  - **Description**: "Customer"
- **ID**: <a href="#customer">customer</a>
- **Optional (Unqualified)**: [ <a href="#active">active</a>, <a href="#address2">address2</a>, <a href="#email">email</a>, <a href="#cell_phone">cell_phone</a>, <a href="#work_phone">work_phone</a>, <a href="#home_phone">home_phone</a> ]
- **Required (Unqualified)**: [ <a href="#id">id</a>, <a href="#first_name">first_name</a>, <a href="#last_name">last_name</a>, <a href="#address">address</a>, <a href="#province">province</a>, <a href="#city">city</a>, <a href="#country">country</a>, <a href="#province">province</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "active" : false,
  "address" : "34 Rue De Fleurs",
  "address2" : "Apt. D",
  "cell_phone" : "+42 34 2344 234",
  "city" : "Tokyo",
  "country" : "FR",
  "email" : "jane@acme.org",
  "first_name" : "Abhul",
  "home_phone" : "+42 34 2344 234",
  "id" : "id1",
  "last_name" : "Smith",
  "province" : "ID",
  "work_phone" : "+42 34 2344 234"
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:active false,
 :address "34 Rue De Fleurs",
 :address2 "Apt. D",
 :cell_phone "+42 34 2344 234",
 :city "Tokyo",
 :country "FR",
 :email "jane@acme.org",
 :first_name "Abhul",
 :home_phone "+42 34 2344 234",
 :id "id1",
 :last_name "Smith",
 :province "ID",
 :work_phone "+42 34 2344 234"}

```

<a name="line_item"></a>

### Line Item

- **ID**: <a href="#line_item">line_item</a>
- **Optional (Unqualified)**: [ <a href="#amount">amount</a>, <a href="#total">total</a>, <a href="#subtotal">subtotal</a>, <a href="#charge">charge</a>, <a href="#fee">fee</a> ]
- **Required (Unqualified)**: [ <a href="#product_id">product_id</a>, <a href="#count">count</a>, <a href="#unit_cost">unit_cost</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "amount" : 3.23,
  "charge" : 42.42,
  "count" : 12,
  "fee" : 3.23,
  "product_id" : "id1",
  "subtotal" : 1.23,
  "total" : 1.23,
  "unit_cost" : 42.42
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:amount 3.23,
 :charge 42.42,
 :count 12,
 :fee 3.23,
 :product_id "id1",
 :subtotal 1.23,
 :total 1.23,
 :unit_cost 42.42}

```

<a name="manufacturer"></a>

### Manufacturer

- **ID**: <a href="#manufacturer">manufacturer</a>
- **Optional (Unqualified)**: [ <a href="#address2">address2</a>, <a href="#contact_email">contact_email</a>, <a href="#phone">phone</a> ]
- **Required (Unqualified)**: [ <a href="#id">id</a>, <a href="#name">name</a>, <a href="#address">address</a>, <a href="#city">city</a>, <a href="#province">province</a>, <a href="#postal_code">postal_code</a>, <a href="#country">country</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "address" : "34 Rue De Fleurs",
  "address2" : "Unit 42",
  "city" : "Paris",
  "contact_email" : "mulan@kemper.io",
  "country" : "US",
  "id" : "abcdef.id",
  "name" : "Name as such",
  "phone" : "+1 (415) 622-1233",
  "postal_code" : "89521",
  "province" : "ID"
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:address "34 Rue De Fleurs",
 :address2 "Unit 42",
 :city "Paris",
 :contact_email "mulan@kemper.io",
 :country "US",
 :id "abcdef.id",
 :name "Name as such",
 :phone "+1 (415) 622-1233",
 :postal_code "89521",
 :province "ID"}

```

<a name="order"></a>

### Order

- **ID**: <a href="#order">order</a>
- **Optional (Unqualified)**: [ <a href="#total">total</a>, <a href="#subtotal">subtotal</a>, <a href="#taxes">taxes</a>, <a href="#fees">fees</a>, <a href="#charges">charges</a> ]
- **Required (Unqualified)**: [ <a href="#id">id</a>, <a href="#line_items">line_items</a>, <a href="#customer_id">customer_id</a>, <a href="#total">total</a>, <a href="#subtotal">subtotal</a>, <a href="#amount">amount</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "amount" : 3.23,
  "charges" : 1.23,
  "customer_id" : "abcdef.id",
  "fees" : 1.23,
  "id" : "id2",
  "line_items" : [ {
    "amount" : 42.42,
    "charge" : 3.23,
    "count" : 4242,
    "fee" : 1.23,
    "product_id" : "id2",
    "subtotal" : 99.99,
    "total" : 3.23,
    "unit_cost" : 3.23
  } ],
  "subtotal" : 1.23,
  "taxes" : 0.34,
  "total" : 0.34
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:amount 3.23,
 :charges 1.23,
 :customer_id "abcdef.id",
 :fees 1.23,
 :id "id2",
 :line_items
 [{:amount 42.42,
   :charge 3.23,
   :count 4242,
   :fee 1.23,
   :product_id "id2",
   :subtotal 99.99,
   :total 3.23,
   :unit_cost 3.23}],
 :subtotal 1.23,
 :taxes 0.34,
 :total 0.34}

```

<a name="person"></a>

### Person

- **Meta**: 
  - **Description**: "Person"
- **ID**: <a href="#person">person</a>
- **Optional (Unqualified)**: [ <a href="#active">active</a>, <a href="#address2">address2</a>, <a href="#email">email</a>, <a href="#cell_phone">cell_phone</a>, <a href="#work_phone">work_phone</a>, <a href="#home_phone">home_phone</a>, <a href="#long_lat">long_lat</a>, <a href="#tags">tags</a> ]
- **Required (Unqualified)**: [ <a href="#id">id</a>, <a href="#first_name">first_name</a>, <a href="#last_name">last_name</a>, <a href="#address">address</a>, <a href="#province">province</a>, <a href="#city">city</a>, <a href="#country">country</a>, <a href="#province">province</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "active" : false,
  "address" : "123 Main St.",
  "address2" : "Unit 42",
  "cell_phone" : "+42 34 2344 234",
  "city" : "Beijing",
  "country" : "CH",
  "email" : "mulan@cater.io",
  "first_name" : "Juan",
  "home_phone" : "+1 (415) 622-1233",
  "id" : "abcdef.id",
  "last_name" : "Smith",
  "long_lat" : [ 144.76126155097882, 44.329334577190366 ],
  "province" : "NV",
  "tags" : [ "tag3" ],
  "work_phone" : "+42 34 2344 234"
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:active false,
 :address "123 Main St.",
 :address2 "Unit 42",
 :cell_phone "+42 34 2344 234",
 :city "Beijing",
 :country "CH",
 :email "mulan@cater.io",
 :first_name "Juan",
 :home_phone "+1 (415) 622-1233",
 :id "abcdef.id",
 :last_name "Smith",
 :long_lat [144.76126155097882 44.329334577190366],
 :province "NV",
 :tags #{"tag3"},
 :work_phone "+42 34 2344 234"}

```

<a name="product"></a>

### Product

- **ID**: <a href="#product">product</a>
- **Optional (Unqualified)**: [ <a href="#sku">sku</a>, <a href="#count">count</a>, <a href="#manufacturer_id">manufacturer_id</a> ]
- **Required (Unqualified)**: [ <a href="#id">id</a>, <a href="#unit_cost">unit_cost</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "count" : 3,
  "id" : "id2",
  "manufacturer_id" : "man2",
  "sku" : "sku99",
  "unit_cost" : 0.34
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:count 3,
 :id "id2",
 :manufacturer_id "man2",
 :sku "sku99",
 :unit_cost 0.34}

```

## Fields

<a name="active"></a>

### Active

- **Meta**: 
  - **Description**: "Active flag (true/false)"
- **ID**: <a href="#active">active</a>
- **Parent ID**: :diction/boolean
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a> ]
- **Type**: :boolean
- **JSON Example**:

```json
false
```
- **EDN Example**:

```clojure
false

```

<a name="address"></a>

### Address

- **Meta**: 
  - **Description**: "Address line."
  - **Sensible Values**: [ "123 Main St.", "34 Rue De Fleurs" ]
- **ID**: <a href="#address">address</a>
- **Max**: 100
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a>, <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"123 Main St."
```
- **EDN Example**:

```clojure
"123 Main St."

```

<a name="address2"></a>

### Address2

- **Meta**: 
  - **Description**: "Address line #2 for units, suites, etc."
  - **Sensible Values**: [ "Unit 42", "#10", "Apt. D", "Suite 4100" ]
- **ID**: <a href="#address2">address2</a>
- **Max**: 100
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a>, <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"#10"
```
- **EDN Example**:

```clojure
"#10"

```

<a name="amount"></a>

### Amount

- **Meta**: 
  - **Description**: "Unit cost."
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#amount">amount</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#line_item">line_item</a>, <a href="#order">order</a> ]
- **Type**: :double
- **JSON Example**:

```json
3.23
```
- **EDN Example**:

```clojure
3.23

```

<a name="cell_phone"></a>

### Cell Phone

- **Meta**: 
  - **Description**: "Cell phone"
  - **Sensible Values**: [ "+1 (415) 622-1233", "+42 34 2344 234" ]
- **ID**: <a href="#cell_phone">cell_phone</a>
- **Max**: 30
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"+42 34 2344 234"
```
- **EDN Example**:

```clojure
"+42 34 2344 234"

```

<a name="charge"></a>

### Charge

- **Meta**: 
  - **Description**: "Charge"
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#charge">charge</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#line_item">line_item</a> ]
- **Type**: :double
- **JSON Example**:

```json
3.23
```
- **EDN Example**:

```clojure
3.23

```

<a name="charges"></a>

### Charges

- **Meta**: 
  - **Description**: "Charges"
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#charges">charges</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#order">order</a> ]
- **Type**: :double
- **JSON Example**:

```json
0.34
```
- **EDN Example**:

```clojure
0.34

```

<a name="city"></a>

### City

- **Meta**: 
  - **Description**: "City or town."
  - **Sensible Values**: [ "Las Vegas", "Paris", "London", "Beijing", "Tokyo" ]
- **ID**: <a href="#city">city</a>
- **Max**: 50
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a>, <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"Beijing"
```
- **EDN Example**:

```clojure
"Beijing"

```

<a name="contact_email"></a>

### Contact Email

- **Meta**: 
  - **Description**: "Contact email address"
  - **Sensible Values**: [ "jane@acme.org", "mulan@kemper.io" ]
- **ID**: <a href="#contact_email">contact_email</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#manufacturer">manufacturer</a> ]
- **Regex Pattern**: ".+@.+..{2,20}"
- **Type**: :string
- **JSON Example**:

```json
"jane@acme.org"
```
- **EDN Example**:

```clojure
"jane@acme.org"

```

<a name="count"></a>

### Count

- **Meta**: 
  - **Description**: "Count."
  - **Sensible Values**: [ 12, 3, 999, 4242, 342 ]
- **ID**: <a href="#count">count</a>
- **Parent ID**: :diction/long
- **Referenced By**: [ <a href="#product">product</a>, <a href="#line_item">line_item</a> ]
- **Type**: :long
- **JSON Example**:

```json
12
```
- **EDN Example**:

```clojure
12

```

<a name="country"></a>

### Country

- **Meta**: 
  - **Description**: "Country code."
  - **Sensible Values**: [ "US", "CH", "FR" ]
- **ID**: <a href="#country">country</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a>, <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"FR"
```
- **EDN Example**:

```clojure
"FR"

```

<a name="customer_id"></a>

### Customer ID

- **Meta**: 
  - **Description**: "Customer ID"
  - **Sensible Values**: [ "id1", "id2", "abcdef.id" ]
- **ID**: <a href="#customer_id">customer_id</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#order">order</a> ]
- **Type**: :string
- **JSON Example**:

```json
"id1"
```
- **EDN Example**:

```clojure
"id1"

```

<a name="delivery_fee"></a>

### Delivery Fee

- **Meta**: 
  - **Description**: "Delivery fee"
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#delivery_fee">delivery_fee</a>
- **Parent ID**: :diction/double
- **Type**: :double
- **JSON Example**:

```json
99.99
```
- **EDN Example**:

```clojure
99.99

```

<a name="email"></a>

### Email

- **Meta**: 
  - **Description**: "Email address"
  - **Sensible Values**: [ "jane@acme.org", "mulan@cater.io" ]
- **ID**: <a href="#email">email</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a> ]
- **Regex Pattern**: ".+@.+..{2,20}"
- **Type**: :string
- **JSON Example**:

```json
"mulan@cater.io"
```
- **EDN Example**:

```clojure
"mulan@cater.io"

```

<a name="fee"></a>

### Fee

- **Meta**: 
  - **Description**: "Fee."
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#fee">fee</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#line_item">line_item</a> ]
- **Type**: :double
- **JSON Example**:

```json
0.34
```
- **EDN Example**:

```clojure
0.34

```

<a name="fees"></a>

### Fees

- **Meta**: 
  - **Description**: "Fees"
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#fees">fees</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#order">order</a> ]
- **Type**: :double
- **JSON Example**:

```json
0.34
```
- **EDN Example**:

```clojure
0.34

```

<a name="first_name"></a>

### First Name

- **Meta**: 
  - **Description**: "First or given name."
  - **Sensible Values**: [ "John", "Jane", "Juan", "Maria", "Abhul" ]
- **ID**: <a href="#first_name">first_name</a>
- **Max**: 50
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"Juan"
```
- **EDN Example**:

```clojure
"Juan"

```

<a name="home_phone"></a>

### Home Phone

- **Meta**: 
  - **Description**: "Home phone"
  - **Sensible Values**: [ "+1 (415) 622-1233", "+42 34 2344 234" ]
- **ID**: <a href="#home_phone">home_phone</a>
- **Max**: 30
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"+42 34 2344 234"
```
- **EDN Example**:

```clojure
"+42 34 2344 234"

```

<a name="id"></a>

### ID

- **Meta**: 
  - **Description**: "Unique identifier."
  - **Sensible Values**: [ "id1", "id2", "abcdef.id" ]
- **ID**: <a href="#id">id</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a>, <a href="#product">product</a>, <a href="#manufacturer">manufacturer</a>, <a href="#order">order</a> ]
- **Type**: :string
- **JSON Example**:

```json
"abcdef.id"
```
- **EDN Example**:

```clojure
"abcdef.id"

```

<a name="ids"></a>

### IDs

- **Meta**: 
  - **Description**: "Unique identifiers."
  - **Sensible Min**: 1
  - **Sensible Max**: 4
- **ID**: <a href="#ids">ids</a>
- **Type**: :vector
- **Vector Of**: <a href="#id">id</a>
- **JSON Example**:

```json
[ "id1", "id2" ]
```
- **EDN Example**:

```clojure
["id1" "id2"]

```

<a name="label"></a>

### Label

- **Meta**: 
  - **Description**: "Label"
  - **Sensible Values**: [ "Label #1", "Label A", "Label Z" ]
- **ID**: <a href="#label">label</a>
- **Max**: 100
- **Min**: 1
- **Parent ID**: :diction/string
- **Type**: :string
- **JSON Example**:

```json
"Label #1"
```
- **EDN Example**:

```clojure
"Label #1"

```

<a name="last_name"></a>

### Last Name

- **Meta**: 
  - **Description**: "Last name or surname."
  - **Sensible Values**: [ "Smith", "Lopez", "Nguyen", "Chang" ]
- **ID**: <a href="#last_name">last_name</a>
- **Max**: 50
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"Chang"
```
- **EDN Example**:

```clojure
"Chang"

```

<a name="latitude"></a>

### Latitude

- **Meta**: 
  - **Description**: "Latitude geo"
- **ID**: <a href="#latitude">latitude</a>
- **Max**: 90.0
- **Min**: -90.0
- **Parent ID**: :diction/double
- **Type**: :double
- **JSON Example**:

```json
-30.721669145899128
```
- **EDN Example**:

```clojure
-30.721669145899128

```

<a name="line_items"></a>

### Line Items

- **Meta**: 
  - **Description**: "Line items."
  - **Sensible Min**: 1
  - **Sensible Max**: 3
- **ID**: <a href="#line_items">line_items</a>
- **Referenced By**: [ <a href="#order">order</a> ]
- **Type**: :vector
- **Vector Of**: <a href="#line_item">line_item</a>
- **JSON Example**:

```json
[ {
  "amount" : 0.34,
  "charge" : 99.99,
  "count" : 3,
  "fee" : 42.42,
  "product_id" : "id2",
  "subtotal" : 99.99,
  "total" : 42.42,
  "unit_cost" : 1.23
}, {
  "amount" : 0.34,
  "charge" : 1.23,
  "count" : 4242,
  "fee" : 42.42,
  "product_id" : "abcdef.id",
  "subtotal" : 0.34,
  "total" : 99.99,
  "unit_cost" : 3.23
}, {
  "amount" : 99.99,
  "charge" : 99.99,
  "count" : 342,
  "fee" : 99.99,
  "product_id" : "id1",
  "subtotal" : 3.23,
  "total" : 1.23,
  "unit_cost" : 99.99
} ]
```
- **EDN Example**:

```clojure
[{:amount 0.34,
  :charge 99.99,
  :count 3,
  :fee 42.42,
  :product_id "id2",
  :subtotal 99.99,
  :total 42.42,
  :unit_cost 1.23}
 {:amount 0.34,
  :charge 1.23,
  :count 4242,
  :fee 42.42,
  :product_id "abcdef.id",
  :subtotal 0.34,
  :total 99.99,
  :unit_cost 3.23}
 {:amount 99.99,
  :charge 99.99,
  :count 342,
  :fee 99.99,
  :product_id "id1",
  :subtotal 3.23,
  :total 1.23,
  :unit_cost 99.99}]

```

<a name="long_lat"></a>

### Long Lat

- **Meta**: 
  - **Description**: "Longitude and latitude pair"
- **ID**: <a href="#long_lat">long_lat</a>
- **Referenced By**: [ <a href="#person">person</a> ]
- **Tuple**: [ <a href="#longitude">longitude</a>, <a href="#latitude">latitude</a> ]
- **Type**: :tuple
- **JSON Example**:

```json
[ -38.51318823888022, -8.94443608927557 ]
```
- **EDN Example**:

```clojure
[-38.51318823888022 -8.94443608927557]

```

<a name="long_lats"></a>

### Long Lats

- **Meta**: 
  - **Description**: "List of longitude/latitude pairs."
  - **Sensible Min**: 1
  - **Sensible Max**: 1
- **ID**: <a href="#long_lats">long_lats</a>
- **Max**: 10
- **Min**: 1
- **Type**: :vector
- **Vector Of**: <a href="#long_lat">long_lat</a>
- **JSON Example**:

```json
[ [ -176.16746196267493, 38.639557357456056 ] ]
```
- **EDN Example**:

```clojure
[[-176.16746196267493 38.639557357456056]]

```

<a name="longitude"></a>

### Longitude

- **Meta**: 
  - **Description**: "Longitude geo (-180 - 180)"
- **ID**: <a href="#longitude">longitude</a>
- **Max**: 180.0
- **Min**: -180.0
- **Parent ID**: :diction/double
- **Type**: :double
- **JSON Example**:

```json
-130.3050015781468
```
- **EDN Example**:

```clojure
-130.3050015781468

```

<a name="manufacturer_id"></a>

### Manufacturer ID

- **Meta**: 
  - **Description**: "Manufacturer ID"
  - **Sensible Values**: [ "man1", "man2", "man3", "man99" ]
- **ID**: <a href="#manufacturer_id">manufacturer_id</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#product">product</a> ]
- **Type**: :string
- **JSON Example**:

```json
"man2"
```
- **EDN Example**:

```clojure
"man2"

```

<a name="name"></a>

### Name

- **Meta**: 
  - **Description**: "Name"
  - **Sensible Values**: [ "Name #1", "Name A", "Name as such" ]
- **ID**: <a href="#name">name</a>
- **Max**: 100
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"Name A"
```
- **EDN Example**:

```clojure
"Name A"

```

<a name="notes"></a>

### Notes

- **Meta**: 
  - **Description**: "Notes"
  - **Sensible Values**: [ "Some notes", "Notes are here", "And more notes" ]
- **ID**: <a href="#notes">notes</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Type**: :string
- **JSON Example**:

```json
"Notes are here"
```
- **EDN Example**:

```clojure
"Notes are here"

```

<a name="notification_email"></a>

### Notification Email

- **Meta**: 
  - **Description**: "Automated notification address"
  - **Sensible Values**: [ "orders@acme.org", "fulfilment@sunshine.io" ]
- **ID**: <a href="#notification_email">notification_email</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Regex Pattern**: ".+@.+..{2,20}"
- **Type**: :string
- **JSON Example**:

```json
"fulfilment@sunshine.io"
```
- **EDN Example**:

```clojure
"fulfilment@sunshine.io"

```

<a name="phone"></a>

### Phone

- **Meta**: 
  - **Description**: "Phone number"
  - **Sensible Values**: [ "+1 (415) 622-1233", "+42 34 2344 234" ]
- **ID**: <a href="#phone">phone</a>
- **Max**: 30
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"+42 34 2344 234"
```
- **EDN Example**:

```clojure
"+42 34 2344 234"

```

<a name="postal_code"></a>

### Postal Code

- **Meta**: 
  - **Description**: "Postal or zip code."
  - **Sensible Values**: [ "89521", "NR14 7PZ" ]
- **ID**: <a href="#postal_code">postal_code</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"89521"
```
- **EDN Example**:

```clojure
"89521"

```

<a name="product_id"></a>

### Product ID

- **Meta**: 
  - **Description**: "Product ID."
  - **Sensible Values**: [ "id1", "id2", "abcdef.id" ]
- **ID**: <a href="#product_id">product_id</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#line_item">line_item</a> ]
- **Type**: :string
- **JSON Example**:

```json
"id1"
```
- **EDN Example**:

```clojure
"id1"

```

<a name="province"></a>

### Province

- **Meta**: 
  - **Description**: "Province or state."
  - **Sensible Values**: [ "NV", "ID", "WY" ]
- **ID**: <a href="#province">province</a>
- **Max**: 50
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a>, <a href="#manufacturer">manufacturer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"WY"
```
- **EDN Example**:

```clojure
"WY"

```

<a name="sales_tax"></a>

### Sales Tax

- **Meta**: 
  - **Description**: "Sales Tax"
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#sales_tax">sales_tax</a>
- **Parent ID**: :diction/double
- **Type**: :double
- **JSON Example**:

```json
99.99
```
- **EDN Example**:

```clojure
99.99

```

<a name="sku"></a>

### Sku

- **Meta**: 
  - **Description**: "SKU"
  - **Sensible Values**: [ "sku1", "sku2", "sku99" ]
- **ID**: <a href="#sku">sku</a>
- **Max**: 50
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#product">product</a> ]
- **Type**: :string
- **JSON Example**:

```json
"sku2"
```
- **EDN Example**:

```clojure
"sku2"

```

<a name="subtotal"></a>

### Subtotal

- **Meta**: 
  - **Description**: "Subtotal."
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#subtotal">subtotal</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#line_item">line_item</a>, <a href="#order">order</a> ]
- **Type**: :double
- **JSON Example**:

```json
3.23
```
- **EDN Example**:

```clojure
3.23

```

<a name="tag"></a>

### Tag

- **Meta**: 
  - **Description**: "Smart tag"
  - **Sensible Values**: [ "tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag99" ]
- **ID**: <a href="#tag">tag</a>
- **Max**: 32
- **Min**: 2
- **Parent ID**: :diction/string
- **Type**: :string
- **JSON Example**:

```json
"tag2"
```
- **EDN Example**:

```clojure
"tag2"

```

<a name="tags"></a>

### Tags

- **Meta**: 
  - **Description**: "Smart tags"
  - **Sensible Min**: 1
  - **Sensible Max**: 4
- **ID**: <a href="#tags">tags</a>
- **Max**: 12
- **Min**: 1
- **Referenced By**: [ <a href="#person">person</a> ]
- **Set Of**: <a href="#tag">tag</a>
- **Type**: :set
- **JSON Example**:

```json
[ "tag5", "tag1" ]
```
- **EDN Example**:

```clojure
#{"tag5" "tag1"}

```

<a name="tax"></a>

### Tax

- **Meta**: 
  - **Description**: "Tax."
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#tax">tax</a>
- **Parent ID**: :diction/double
- **Type**: :double
- **JSON Example**:

```json
0.34
```
- **EDN Example**:

```clojure
0.34

```

<a name="taxes"></a>

### Taxes

- **Meta**: 
  - **Description**: "Taxes"
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#taxes">taxes</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#order">order</a> ]
- **Type**: :double
- **JSON Example**:

```json
42.42
```
- **EDN Example**:

```clojure
42.42

```

<a name="total"></a>

### Total

- **Meta**: 
  - **Description**: "Total"
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#total">total</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#line_item">line_item</a>, <a href="#order">order</a> ]
- **Type**: :double
- **JSON Example**:

```json
3.23
```
- **EDN Example**:

```clojure
3.23

```

<a name="unit_cost"></a>

### Unit Cost

- **Meta**: 
  - **Description**: "Unit cost."
  - **Sensible Values**: [ 3.23, 0.34, 99.99, 42.42, 1.23 ]
- **ID**: <a href="#unit_cost">unit_cost</a>
- **Parent ID**: :diction/double
- **Referenced By**: [ <a href="#product">product</a>, <a href="#line_item">line_item</a> ]
- **Type**: :double
- **JSON Example**:

```json
1.23
```
- **EDN Example**:

```clojure
1.23

```

<a name="work_phone"></a>

### Work Phone

- **Meta**: 
  - **Description**: "Work phone"
  - **Sensible Values**: [ "+1 (415) 622-1233", "+42 34 2344 234" ]
- **ID**: <a href="#work_phone">work_phone</a>
- **Max**: 30
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#person">person</a>, <a href="#customer">customer</a> ]
- **Type**: :string
- **JSON Example**:

```json
"+42 34 2344 234"
```
- **EDN Example**:

```clojure
"+42 34 2344 234"

```

