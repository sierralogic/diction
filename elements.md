# Diction Declarative Data Elements
*generated: Fri Mar 27 09:55:15 PDT 2020*

## Document Index

- <a href="#diction_element_data_element">diction-element/data-element</a>
- <a href="#diction_element_meta">diction-element/meta</a>

## Field Index

- <a href="#diction_element_any">diction-element/any</a>
- <a href="#diction_element_description">diction-element/description</a>
- <a href="#diction_element_enum">diction-element/enum</a>
- <a href="#diction_element_field">diction-element/field</a>
- <a href="#diction_element_fields">diction-element/fields</a>
- <a href="#diction_element_id">diction-element/id</a>
- <a href="#diction_element_label">diction-element/label</a>
- <a href="#diction_element_max">diction-element/max</a>
- <a href="#diction_element_min">diction-element/min</a>
- <a href="#diction_element_optional">diction-element/optional</a>
- <a href="#diction_element_optional_un">diction-element/optional-un</a>
- <a href="#diction_element_poly_vector_of">diction-element/poly-vector-of</a>
- <a href="#diction_element_regex">diction-element/regex</a>
- <a href="#diction_element_regex_pattern">diction-element/regex-pattern</a>
- <a href="#diction_element_required">diction-element/required</a>
- <a href="#diction_element_required_un">diction-element/required-un</a>
- <a href="#diction_element_sensible_max">diction-element/sensible-max</a>
- <a href="#diction_element_sensible_min">diction-element/sensible-min</a>
- <a href="#diction_element_sensible_value">diction-element/sensible-value</a>
- <a href="#diction_element_sensible_values">diction-element/sensible-values</a>
- <a href="#diction_element_set_of">diction-element/set-of</a>
- <a href="#diction_element_tag">diction-element/tag</a>
- <a href="#diction_element_tags">diction-element/tags</a>
- <a href="#diction_element_tuple">diction-element/tuple</a>
- <a href="#diction_element_type">diction-element/type</a>
- <a href="#diction_element_vector_of">diction-element/vector-of</a>


## Documents

<a name="diction_element_data_element"></a>

### Diction Element / Data Element

- **Meta**: 
  - **Description**: "The declarative data element map used to register a data element."
- **ID**: <a href="#diction_element_data_element">diction-element/data-element</a>
- **Optional (Unqualified)**: [ <a href="#diction_element_required">diction-element/required</a>, <a href="#diction_element_required_un">diction-element/required-un</a>, <a href="#diction_element_optional">diction-element/optional</a>, <a href="#diction_element_optional_un">diction-element/optional-un</a>, <a href="#diction_element_meta">diction-element/meta</a>, <a href="#diction_element_min">diction-element/min</a>, <a href="#diction_element_max">diction-element/max</a>, <a href="#diction_element_vector_of">diction-element/vector-of</a>, <a href="#diction_element_set_of">diction-element/set-of</a>, <a href="#diction_element_poly_vector_of">diction-element/poly-vector-of</a>, <a href="#diction_element_enum">diction-element/enum</a>, <a href="#diction_element_tuple">diction-element/tuple</a>, <a href="#diction_element_regex_pattern">diction-element/regex-pattern</a>, <a href="#diction_element_regex">diction-element/regex</a> ]
- **Required (Unqualified)**: [ <a href="#diction_element_id">diction-element/id</a>, <a href="#diction_element_type">diction-element/type</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "enum" : "int",
  "id" : "element99",
  "max" : 10,
  "meta" : {
    "description" : "description2",
    "label" : "label2",
    "sensible-max" : 5,
    "sensible-min" : 0,
    "sensible-values" : [ null, "any" ],
    "tags" : [ "tag5", "tag1" ]
  },
  "min" : 50,
  "optional" : [ "ns/alt-phone", "ns/tags", "ns/twitter", "ns/linkedin" ],
  "optional-un" : [ "alt-phone", "tags", "twitter", "linkedin" ],
  "poly-vector-of" : "string",
  "regex" : "\\d{7}",
  "regex-pattern" : "\\d{7}",
  "required" : [ "ns/id", "ns/label", "ns/active" ],
  "required-un" : [ "id", "name", "dob" ],
  "set-of" : "long",
  "tuple" : "uuid",
  "type" : "joda",
  "vector-of" : "keyword"
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:enum :int,
 :id :element99,
 :max 10,
 :meta
 {:description "description2",
  :label "label2",
  :sensible-max 5,
  :sensible-min 0,
  :sensible-values [nil "any"],
  :tags #{"tag5" "tag1"}},
 :min 50,
 :optional [:ns/alt-phone :ns/tags :ns/twitter :ns/linkedin],
 :optional-un [:alt-phone :tags :twitter :linkedin],
 :poly-vector-of :string,
 :regex #"\d{7}",
 :regex-pattern "\\d{7}",
 :required [:ns/id :ns/label :ns/active],
 :required-un [:id :name :dob],
 :set-of :long,
 :tuple :uuid,
 :type :joda,
 :vector-of :keyword}

```

<a name="diction_element_meta"></a>

### Diction Element / Meta

- **Meta**: 
  - **Description**: "Metadata of the element with important hints and settings."
- **ID**: <a href="#diction_element_meta">diction-element/meta</a>
- **Optional (Unqualified)**: [ <a href="#diction_element_description">diction-element/description</a>, <a href="#diction_element_label">diction-element/label</a>, <a href="#diction_element_sensible_values">diction-element/sensible-values</a>, <a href="#diction_element_sensible_min">diction-element/sensible-min</a>, <a href="#diction_element_sensible_max">diction-element/sensible-max</a>, <a href="#diction_element_tags">diction-element/tags</a> ]
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :document
- **JSON Example**: (required and optional field(s))

```json
{
  "description" : "description99",
  "label" : "label2",
  "sensible-max" : 50,
  "sensible-min" : 50,
  "sensible-values" : [ 1.2 ],
  "tags" : [ "tag5", "tag4", "tag1" ]
}
```
- **EDN Example**: (required and optional field(s))

```clojure
{:description "description99",
 :label "label2",
 :sensible-max 50,
 :sensible-min 50,
 :sensible-values [1.2],
 :tags #{"tag5" "tag4" "tag1"}}

```

## Fields

<a name="diction_element_any"></a>

### Diction Element / Any

- **Meta**: 
  - **Description**: "Any element type.  Used for supporting unknown types."
  - **Sensible Values**: [ , 0, 1.2, "any", true, false, :any-kw ]
- **ID**: <a href="#diction_element_any">diction-element/any</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Type**: :any
- **JSON Example**:

```json
true
```
- **EDN Example**:

```clojure
true

```

<a name="diction_element_description"></a>

### Diction Element / Description

- **Meta**: 
  - **Description**: "Description of element."
  - **Sensible Values**: [ "description1", "description2", "description99" ]
- **ID**: <a href="#diction_element_description">diction-element/description</a>
- **Max**: 100
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_meta">diction-element/meta</a> ]
- **Type**: :string
- **JSON Example**:

```json
"description1"
```
- **EDN Example**:

```clojure
"description1"

```

<a name="diction_element_enum"></a>

### Diction Element / Enum

- **Meta**: 
  - **Description**: "An enumerated data element, meaning only explicit list of values allowed."
  - **Sensible Values**: [ :double, :float, :int, :integer, :joda, :date, :datetime, :keyword, :kw, :long, :text, :string, :uuid ]
- **ID**: <a href="#diction_element_enum">diction-element/enum</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :keyword
- **JSON Example**:

```json
"date"
```
- **EDN Example**:

```clojure
:date

```

<a name="diction_element_field"></a>

### Diction Element / Field

- **Meta**: 
  - **Description**: "The data element id."
  - **Sensible Values**: [ :element1, :element2, :element99 ]
- **ID**: <a href="#diction_element_field">diction-element/field</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Type**: :keyword
- **JSON Example**:

```json
"element99"
```
- **EDN Example**:

```clojure
:element99

```

<a name="diction_element_fields"></a>

### Diction Element / Fields

- **Meta**: 
  - **Sensible Min**: 1
  - **Sensible Max**: 6
- **ID**: <a href="#diction_element_fields">diction-element/fields</a>
- **Type**: :vector
- **Vector Of**: <a href="#diction_element_field">diction-element/field</a>
- **JSON Example**:

```json
[ "element1", "element2", "element2", "element99" ]
```
- **EDN Example**:

```clojure
[:element1 :element2 :element2 :element99]

```

<a name="diction_element_id"></a>

### Diction Element / ID

- **Meta**: 
  - **Description**: "The data element id."
  - **Sensible Values**: [ :element1, :element2, :element99 ]
- **ID**: <a href="#diction_element_id">diction-element/id</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :keyword
- **JSON Example**:

```json
"element99"
```
- **EDN Example**:

```clojure
:element99

```

<a name="diction_element_label"></a>

### Diction Element / Label

- **Meta**: 
  - **Description**: "Label of an element."
  - **Sensible Values**: [ "label1", "label2", "label99" ]
- **ID**: <a href="#diction_element_label">diction-element/label</a>
- **Max**: 100
- **Min**: 1
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_meta">diction-element/meta</a> ]
- **Type**: :string
- **JSON Example**:

```json
"label2"
```
- **EDN Example**:

```clojure
"label2"

```

<a name="diction_element_max"></a>

### Diction Element / Max

- **Meta**: 
  - **Description**: "The maximum length or value of a field."
  - **Sensible Values**: [ 5, 10, 50 ]
- **ID**: <a href="#diction_element_max">diction-element/max</a>
- **Max**: 1000
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :integer
- **JSON Example**:

```json
50
```
- **EDN Example**:

```clojure
50

```

<a name="diction_element_min"></a>

### Diction Element / Min

- **Meta**: 
  - **Description**: "The minimum length or value of a field."
  - **Sensible Values**: [ 0, 1, 10, 50 ]
- **ID**: <a href="#diction_element_min">diction-element/min</a>
- **Max**: 1000
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :integer
- **JSON Example**:

```json
50
```
- **EDN Example**:

```clojure
50

```

<a name="diction_element_optional"></a>

### Diction Element / Optional

- **Meta**: 
  - **Sensible Min**: 1
  - **Sensible Max**: 6
  - **Description**: "Optional qualified elements (namespaced ids) for a document/entity."
  - **Sensible Values**: [ [:ns/alt-phone :ns/tags :ns/twitter :ns/linkedin], [:ns/email :ns/alt-email :ns/address2] ]
- **ID**: <a href="#diction_element_optional">diction-element/optional</a>
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :vector
- **Vector Of**: <a href="#diction_element_field">diction-element/field</a>
- **JSON Example**:

```json
[ "ns/email", "ns/alt-email", "ns/address2" ]
```
- **EDN Example**:

```clojure
[:ns/email :ns/alt-email :ns/address2]

```

<a name="diction_element_optional_un"></a>

### Diction Element / Optional Un

- **Meta**: 
  - **Sensible Min**: 1
  - **Sensible Max**: 6
  - **Description**: "Optional unqualified elements (un-dnamespaced ids) for a document/entity."
  - **Sensible Values**: [ [:alt-phone :tags :twitter :linkedin], [:email :alt-email :address2] ]
- **ID**: <a href="#diction_element_optional_un">diction-element/optional-un</a>
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :vector
- **Vector Of**: <a href="#diction_element_field">diction-element/field</a>
- **JSON Example**:

```json
[ "alt-phone", "tags", "twitter", "linkedin" ]
```
- **EDN Example**:

```clojure
[:alt-phone :tags :twitter :linkedin]

```

<a name="diction_element_poly_vector_of"></a>

### Diction Element / Poly Vector Of

- **Meta**: 
  - **Description**: "A vector/list data element of one or more types."
  - **Sensible Values**: [ :double, :float, :int, :integer, :joda, :date, :datetime, :keyword, :kw, :long, :text, :string, :uuid ]
- **ID**: <a href="#diction_element_poly_vector_of">diction-element/poly-vector-of</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :keyword
- **JSON Example**:

```json
"int"
```
- **EDN Example**:

```clojure
:int

```

<a name="diction_element_regex"></a>

### Diction Element / Regex

- **Meta**: 
  - **Description**: "Regular expression as a Pattern object."
  - **Sensible Values**: [ \d{7} ]
- **ID**: <a href="#diction_element_regex">diction-element/regex</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :any
- **JSON Example**:

```json
"\\d{7}"
```
- **EDN Example**:

```clojure
#"\d{7}"

```

<a name="diction_element_regex_pattern"></a>

### Diction Element / Regex Pattern

- **Meta**: 
  - **Description**: "Regular expression pattern as a string."
  - **Sensible Values**: [ "\d{7}", "\d\s[abc]" ]
- **ID**: <a href="#diction_element_regex_pattern">diction-element/regex-pattern</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :string
- **JSON Example**:

```json
"\\d{7}"
```
- **EDN Example**:

```clojure
"\\d{7}"

```

<a name="diction_element_required"></a>

### Diction Element / Required

- **Meta**: 
  - **Sensible Min**: 1
  - **Sensible Max**: 6
  - **Description**: "Required qualified elements (namespaced ids) for a document/entity."
  - **Sensible Values**: [ [:ns/id :ns/name :ns/dob], [:ns/id :ns/label :ns/active] ]
- **ID**: <a href="#diction_element_required">diction-element/required</a>
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :vector
- **Vector Of**: <a href="#diction_element_field">diction-element/field</a>
- **JSON Example**:

```json
[ "ns/id", "ns/label", "ns/active" ]
```
- **EDN Example**:

```clojure
[:ns/id :ns/label :ns/active]

```

<a name="diction_element_required_un"></a>

### Diction Element / Required Un

- **Meta**: 
  - **Sensible Min**: 1
  - **Sensible Max**: 6
  - **Description**: "Required unqualified elements (un-namespaced ids) for a document/entity."
  - **Sensible Values**: [ [:id :name :dob], [:id :label :active] ]
- **ID**: <a href="#diction_element_required_un">diction-element/required-un</a>
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :vector
- **Vector Of**: <a href="#diction_element_field">diction-element/field</a>
- **JSON Example**:

```json
[ "id", "name", "dob" ]
```
- **EDN Example**:

```clojure
[:id :name :dob]

```

<a name="diction_element_sensible_max"></a>

### Diction Element / Sensible Max

- **Meta**: 
  - **Description**: "The maximum length or value of a sensible field."
  - **Sensible Values**: [ 5, 10, 50 ]
- **ID**: <a href="#diction_element_sensible_max">diction-element/sensible-max</a>
- **Max**: 1000
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_meta">diction-element/meta</a> ]
- **Type**: :integer
- **JSON Example**:

```json
5
```
- **EDN Example**:

```clojure
5

```

<a name="diction_element_sensible_min"></a>

### Diction Element / Sensible Min

- **Meta**: 
  - **Description**: "The minimum length or value of a sensible field."
  - **Sensible Values**: [ 0, 1, 10, 50 ]
- **ID**: <a href="#diction_element_sensible_min">diction-element/sensible-min</a>
- **Max**: 1000
- **Min**: 0
- **Parent ID**: :diction/string
- **Referenced By**: [ <a href="#diction_element_meta">diction-element/meta</a> ]
- **Type**: :integer
- **JSON Example**:

```json
50
```
- **EDN Example**:

```clojure
50

```

<a name="diction_element_sensible_value"></a>

### Diction Element / Sensible Value

- **Meta**: 
  - **Description**: "Sensible value used for generated, human-readable documentation of the element."
  - **Sensible Values**: [ , 0, 1.2, "any", true, false, :any-kw ]
- **ID**: <a href="#diction_element_sensible_value">diction-element/sensible-value</a>
- **Max**: 64
- **Min**: 0
- **Parent ID**: :diction/string
- **Type**: :any
- **JSON Example**:

```json
null
```
- **EDN Example**:

```clojure
nil

```

<a name="diction_element_sensible_values"></a>

### Diction Element / Sensible Values

- **Meta**: 
  - **Description**: "Sensible values of the data element for generated, human-readable documentation."
  - **Sensible Min**: 1
  - **Sensible Max**: 4
- **ID**: <a href="#diction_element_sensible_values">diction-element/sensible-values</a>
- **Referenced By**: [ <a href="#diction_element_meta">diction-element/meta</a> ]
- **Type**: :vector
- **Vector Of**: <a href="#diction_element_sensible_value">diction-element/sensible-value</a>
- **JSON Example**:

```json
[ 1.2, false ]
```
- **EDN Example**:

```clojure
[1.2 false]

```

<a name="diction_element_set_of"></a>

### Diction Element / Set Of

- **Meta**: 
  - **Description**: "A set data element (with no duplicated) of a single type."
  - **Sensible Values**: [ :double, :float, :int, :integer, :joda, :date, :datetime, :keyword, :kw, :long, :text, :string, :uuid ]
- **ID**: <a href="#diction_element_set_of">diction-element/set-of</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :keyword
- **JSON Example**:

```json
"date"
```
- **EDN Example**:

```clojure
:date

```

<a name="diction_element_tag"></a>

### Diction Element / Tag

- **Meta**: 
  - **Description**: "A textual tag element."
  - **Sensible Values**: [ "tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag99" ]
- **ID**: <a href="#diction_element_tag">diction-element/tag</a>
- **Max**: 32
- **Min**: 1
- **Parent ID**: :diction/string
- **Type**: :string
- **JSON Example**:

```json
"tag6"
```
- **EDN Example**:

```clojure
"tag6"

```

<a name="diction_element_tags"></a>

### Diction Element / Tags

- **Meta**: 
  - **Description**: "A list of tags element."
  - **Sensible Min**: 1
  - **Sensible Max**: 4
- **ID**: <a href="#diction_element_tags">diction-element/tags</a>
- **Max**: 100
- **Min**: 1
- **Referenced By**: [ <a href="#diction_element_meta">diction-element/meta</a> ]
- **Set Of**: <a href="#diction_element_tag">diction-element/tag</a>
- **Type**: :set
- **JSON Example**:

```json
[ "tag5", "tag6" ]
```
- **EDN Example**:

```clojure
#{"tag5" "tag6"}

```

<a name="diction_element_tuple"></a>

### Diction Element / Tuple

- **Meta**: 
  - **Description**: "A tuple (list) of a single type."
  - **Sensible Values**: [ :double, :float, :int, :integer, :joda, :date, :datetime, :keyword, :kw, :long, :text, :string, :uuid ]
- **ID**: <a href="#diction_element_tuple">diction-element/tuple</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :keyword
- **JSON Example**:

```json
"long"
```
- **EDN Example**:

```clojure
:long

```

<a name="diction_element_type"></a>

### Diction Element / Type

- **Meta**: 
  - **Description**: "The data element type (e.g. `:string` or `:double`)."
  - **Sensible Values**: [ :double, :document, :map, :entity, :enum, :float, :int, :integer, :joda, :date, :datetime, :keyword, :kw, :long, :text, :string, :uuid, :vector, :list, :tuple, :poly-vector ]
- **ID**: <a href="#diction_element_type">diction-element/type</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :keyword
- **JSON Example**:

```json
"text"
```
- **EDN Example**:

```clojure
:text

```

<a name="diction_element_vector_of"></a>

### Diction Element / Vector Of

- **Meta**: 
  - **Description**: "A vector/list data element of single type."
  - **Sensible Values**: [ :double, :float, :int, :integer, :joda, :date, :datetime, :keyword, :kw, :long, :text, :string, :uuid ]
- **ID**: <a href="#diction_element_vector_of">diction-element/vector-of</a>
- **Max**: 64
- **Min**: 1
- **Parent ID**: :diction/keyword
- **Referenced By**: [ <a href="#diction_element_data_element">diction-element/data-element</a> ]
- **Type**: :keyword
- **JSON Example**:

```json
"integer"
```
- **EDN Example**:

```clojure
:integer

```

