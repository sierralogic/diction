# Demo Data Dictionary
*generated: Mon Mar 30 14:37:08 PDT 2020*

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
- <a href="#field_of_odd_pos_int">field-of-odd-pos-int</a>
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
  "cell_phone" : "+1 (415) 622-1233",
  "city" : "Las Vegas",
  "country" : "FR",
  "email" : "mulan@cater.io",
  "first_name" : "John",
  "home_phone" : "+1 (415) 622-1233",
  "id" : "abcdef.id",
  "last_name" : "Lopez",
  "province" : "ID",
  "work_phone" : "+1 (415) 622-1233"
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:active false,
 :address "34 Rue De Fleurs",
 :address2 "Apt. D",
 :cell_phone "+1 (415) 622-1233",
 :city "Las Vegas",
 :country "FR",
 :email "mulan@cater.io",
 :first_name "John",
 :home_phone "+1 (415) 622-1233",
 :id "abcdef.id",
 :last_name "Lopez",
 :province "ID",
 :work_phone "+1 (415) 622-1233"}

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
  "charge" : 3.23,
  "count" : 3,
  "fee" : 3.23,
  "product_id" : "id2",
  "subtotal" : 42.42,
  "total" : 42.42,
  "unit_cost" : 1.23
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:amount 3.23,
 :charge 3.23,
 :count 3,
 :fee 3.23,
 :product_id "id2",
 :subtotal 42.42,
 :total 42.42,
 :unit_cost 1.23}

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
  "address" : "123 Main St.",
  "address2" : "Apt. D",
  "city" : "Beijing",
  "contact_email" : "jane@acme.org",
  "country" : "US",
  "id" : "id1",
  "name" : "Name #1",
  "phone" : "+1 (415) 622-1233",
  "postal_code" : "NR14 7PZ",
  "province" : "WY"
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:address "123 Main St.",
 :address2 "Apt. D",
 :city "Beijing",
 :contact_email "jane@acme.org",
 :country "US",
 :id "id1",
 :name "Name #1",
 :phone "+1 (415) 622-1233",
 :postal_code "NR14 7PZ",
 :province "WY"}

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
  "amount" : 99.99,
  "charges" : 42.42,
  "customer_id" : "abcdef.id",
  "fees" : 0.34,
  "id" : "id1",
  "line_items" : [ {
    "amount" : 99.99,
    "charge" : 42.42,
    "count" : 12,
    "fee" : 0.34,
    "product_id" : "id2",
    "subtotal" : 42.42,
    "total" : 3.23,
    "unit_cost" : 42.42
  }, {
    "amount" : 3.23,
    "charge" : 99.99,
    "count" : 999,
    "fee" : 1.23,
    "product_id" : "abcdef.id",
    "subtotal" : 42.42,
    "total" : 3.23,
    "unit_cost" : 0.34
  } ],
  "subtotal" : 1.23,
  "taxes" : 42.42,
  "total" : 99.99
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:amount 99.99,
 :charges 42.42,
 :customer_id "abcdef.id",
 :fees 0.34,
 :id "id1",
 :line_items
 [{:amount 99.99,
   :charge 42.42,
   :count 12,
   :fee 0.34,
   :product_id "id2",
   :subtotal 42.42,
   :total 3.23,
   :unit_cost 42.42}
  {:amount 3.23,
   :charge 99.99,
   :count 999,
   :fee 1.23,
   :product_id "abcdef.id",
   :subtotal 42.42,
   :total 3.23,
   :unit_cost 0.34}],
 :subtotal 1.23,
 :taxes 42.42,
 :total 99.99}

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
  "active" : true,
  "address" : "34 Rue De Fleurs",
  "address2" : "Suite 4100",
  "cell_phone" : "+1 (415) 622-1233",
  "city" : "Las Vegas",
  "country" : "CH",
  "email" : "jane@acme.org",
  "first_name" : "John",
  "home_phone" : "+1 (415) 622-1233",
  "id" : "id1",
  "last_name" : "Lopez",
  "long_lat" : [ -117.03904572735306, -65.41774833059156 ],
  "province" : "ID",
  "tags" : [ "tag2" ],
  "work_phone" : "+1 (415) 622-1233"
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:active true,
 :address "34 Rue De Fleurs",
 :address2 "Suite 4100",
 :cell_phone "+1 (415) 622-1233",
 :city "Las Vegas",
 :country "CH",
 :email "jane@acme.org",
 :first_name "John",
 :home_phone "+1 (415) 622-1233",
 :id "id1",
 :last_name "Lopez",
 :long_lat [-117.03904572735306 -65.41774833059156],
 :province "ID",
 :tags #{"tag2"},
 :work_phone "+1 (415) 622-1233"}

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
  "count" : 4242,
  "id" : "abcdef.id",
  "manufacturer_id" : "man3",
  "sku" : "sku1",
  "unit_cost" : 99.99
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:count 4242,
 :id "abcdef.id",
 :manufacturer_id "man3",
 :sku "sku1",
 :unit_cost 99.99}

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
42.42
```
- **EDN Example**:

```clojure
42.42

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
"+1 (415) 622-1233"
```
- **EDN Example**:

```clojure
"+1 (415) 622-1233"

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
1.23
```
- **EDN Example**:

```clojure
1.23

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
"Tokyo"
```
- **EDN Example**:

```clojure
"Tokyo"

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
4242
```
- **EDN Example**:

```clojure
4242

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
"US"
```
- **EDN Example**:

```clojure
"US"

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
"abcdef.id"
```
- **EDN Example**:

```clojure
"abcdef.id"

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
"jane@acme.org"
```
- **EDN Example**:

```clojure
"jane@acme.org"

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
99.99
```
- **EDN Example**:

```clojure
99.99

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
42.42
```
- **EDN Example**:

```clojure
42.42

```

<a name="field_of_odd_pos_int"></a>

### Field Of Odd Pos Int

- **ID**: <a href="#field_of_odd_pos_int">field-of-odd-pos-int</a>
- **Type**: :odd-pos-int
- **JSON Example**:

```json
1473873039
```
- **EDN Example**:

```clojure
1473873039

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
"John"
```
- **EDN Example**:

```clojure
"John"

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
"+1 (415) 622-1233"
```
- **EDN Example**:

```clojure
"+1 (415) 622-1233"

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
[ "abcdef.id" ]
```
- **EDN Example**:

```clojure
["abcdef.id"]

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
"Label A"
```
- **EDN Example**:

```clojure
"Label A"

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
-48.32686234487875
```
- **EDN Example**:

```clojure
-48.32686234487875

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
  "amount" : 1.23,
  "charge" : 99.99,
  "count" : 342,
  "fee" : 0.34,
  "product_id" : "id1",
  "subtotal" : 42.42,
  "total" : 0.34,
  "unit_cost" : 42.42
} ]
```
- **EDN Example**:

```clojure
[{:amount 1.23,
  :charge 99.99,
  :count 342,
  :fee 0.34,
  :product_id "id1",
  :subtotal 42.42,
  :total 0.34,
  :unit_cost 42.42}]

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
[ 116.41428138717629, 68.32544208467141 ]
```
- **EDN Example**:

```clojure
[116.41428138717629 68.32544208467141]

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
[ [ -134.7779442431719, 15.293036401186953 ] ]
```
- **EDN Example**:

```clojure
[[-134.7779442431719 15.293036401186953]]

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
84.83252874119438
```
- **EDN Example**:

```clojure
84.83252874119438

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
"man1"
```
- **EDN Example**:

```clojure
"man1"

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
"Name as such"
```
- **EDN Example**:

```clojure
"Name as such"

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
"Some notes"
```
- **EDN Example**:

```clojure
"Some notes"

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
"orders@acme.org"
```
- **EDN Example**:

```clojure
"orders@acme.org"

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
"NR14 7PZ"
```
- **EDN Example**:

```clojure
"NR14 7PZ"

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
"abcdef.id"
```
- **EDN Example**:

```clojure
"abcdef.id"

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
"ID"
```
- **EDN Example**:

```clojure
"ID"

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
1.23
```
- **EDN Example**:

```clojure
1.23

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
"sku99"
```
- **EDN Example**:

```clojure
"sku99"

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
42.42
```
- **EDN Example**:

```clojure
42.42

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
[ "tag5", "tag6" ]
```
- **EDN Example**:

```clojure
#{"tag5" "tag6"}

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
3.23
```
- **EDN Example**:

```clojure
3.23

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
1.23
```
- **EDN Example**:

```clojure
1.23

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
42.42
```
- **EDN Example**:

```clojure
42.42

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
42.42
```
- **EDN Example**:

```clojure
42.42

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
"+1 (415) 622-1233"
```
- **EDN Example**:

```clojure
"+1 (415) 622-1233"

```

