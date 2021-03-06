(ns diction.demo
  (:require [diction.core :refer [clone!
                                  info data-dictionary explain
                                  long!
                                  pos-double! neg-double! double!
                                  pos-float! neg-float! float!
                                  pos-int! neg-int! int!
                                  string!
                                  boolean!
                                  force-generate
                                  vector! set-of! tuple! poly-vector! enum!
                                  document!] :as diction]
            [diction.documentation :as doc]
            [diction.util :as util]))


(def dictionary
  "Demo data dictionary as list of data element maps."
  [

   {:id :first_name
    :type :string
    :min 1
    :max 50
    :meta {:description "First or given name."
           :sensible-values ["John" "Jane" "Juan" "Maria" "Abhul"]}}

   {:id :last_name
    :type :string
    :min 1
    :max 50
    :meta {:description "Last name or surname."
           :sensible-values ["Smith" "Lopez" "Nguyen" "Chang"]}}

   {:id :address
    :type :string
    :min 1
    :max 100
    :meta {:description "Address line."
           :sensible-values ["123 Main St." "34 Rue De Fleurs"]}}

   {:id :address2
    :type :string
    :min 0
    :max 100
    :meta {:description "Address line #2 for units, suites, etc."
           :sensible-values ["Unit 42" "#10" "Apt. D" "Suite 4100"]}}

   {:id :city
    :type :string
    :min 1
    :max 50
    :meta {:description "City or town."
           :sensible-values ["Las Vegas" "Paris" "London" "Beijing" "Tokyo"]}}

   {:id :province
    :type :string
    :min 1
    :max 50
    :meta {:description "Province or state."
           :sensible-values ["NV" "ID" "WY"]}}

   {:id :postal_code
    :type :string
    :meta {:description "Postal or zip code."
           :sensible-values ["89521" "NR14 7PZ"]}}

   {:id :country
    :type :string
    :meta {:description "Country code."
           :sensible-values ["US" "CH" "FR"]}}

   {:id :email
    :meta {:description "Email address"
           :sensible-values ["jane@acme.org" "mulan@cater.io"]}
    :type :string
    :regex-pattern ".+@.+..{2,20}"}

   {:id :phone
    :meta {:description "Phone number"
           :sensible-values ["+1 (415) 622-1233" "+42 34 2344 234"]}
    :type :string
    :min 1
    :max 30}

   {:id :cell_phone
    :clone :phone
    :meta {:description "Cell phone"}}

   {:id :home_phone
    :clone :phone
    :meta {:description "Home phone"}}

   {:id :work_phone
    :clone :phone
    :meta {:description "Work phone"}}

   {:id :label
    :meta {:description "Label"
           :sensible-values ["Label #1" "Label A" "Label Z"]}
    :type :string
    :min 1
    :max 100}

   {:id :name
    :clone :label
    :meta {:description "Name"
           :sensible-values ["Name #1" "Name A" "Name as such"]}}

   {:id :contact_email
    :clone :email
    :meta {:description "Contact email address"
           :sensible-values ["jane@acme.org" "mulan@kemper.io"]}}

   {:id :notification_email
    :clone :email
    :meta {:description "Automated notification address"
           :sensible-values ["orders@acme.org" "fulfilment@sunshine.io"]}}

   {:id :notes
    :type :string
    :meta {:description "Notes"
           :sensible-values ["Some notes" "Notes are here" "And more notes"]}}

   {:id :active
    :meta {:description "Active flag (true/false)"}
    :type :boolean}

   {:id :latitude
    :meta {:description "Latitude geo"}
    :type :double
    :min -90.0
    :max 90.0}

   {:id :longitude
    :meta {:description "Longitude geo (-180 - 180)"}
    :type :double
    :min -180.0
    :max 180.0}

   {:id :long_lat
    :meta {:description "Longitude and latitude pair"}
    :type :tuple
    :tuple [:longitude :latitude]}

   {:id :long_lats
    :meta {:description "List of longitude/latitude pairs."
           :sensible-min 1
           :sensible-max 1}
    :type :vector
    :vector-of :long_lat
    :min 1
    :max 10}

   {:id :tag
    :meta {:description "Smart tag"
           :sensible-values ["tag1" "tag2" "tag3"
                             "tag4" "tag5" "tag6"
                             "tag99"]}
    :type :string
    :min 2
    :max 32}

   {:id :tags
    :meta {:description "Smart tags"
           :sensible-min 1
           :sensible-max 4}
    :type :set
    :set-of :tag
    :min 1
    :max 12}

   {:id :id
    :type :string
    :meta {:description "Unique identifier."
           :sensible-values ["id1" "id2" "abcdef.id"]}}

   {:id :ids
    :meta {:description "Unique identifiers."
           :sensible-min 1
           :sensible-max 4}
    :type :vector
    :vector-of :id}

   {:id :unit_cost
    :type :double
    :meta {:description "Unit cost."
           :sensible-values [3.23 0.34 99.99 42.42 1.23]}}

   {:id :amount
    :type :double
    :meta {:description "Unit cost."
           :sensible-values [3.23 0.34 99.99 42.42 1.23]}}

   {:id :total
    :type :double
    :meta {:description "Total"
           :sensible-values [3.23 0.34 99.99 42.42 1.23]}}

   {:id :subtotal
    :type :double
    :meta {:description "Subtotal."
           :sensible-values [3.23 0.34 99.99 42.42 1.23]}}

   {:id :count
    :type :long
    :meta {:description "Count."
           :sensible-values [12 3 999 4242 342]}}

   {:id :tax
    :type :double
    :meta {:description "Tax."
           :sensible-values [3.23 0.34 99.99 42.42 1.23]}}

   {:id :sales_tax
    :clone :tax
    :meta {:description "Sales Tax"}}

   {:id :taxes
    :clone :tax
    :meta {:description "Taxes"}}

   {:id :fee
    :type :double
    :meta {:description "Fee."
           :sensible-values [3.23 0.34 99.99 42.42 1.23]}}

   {:id :delivery_fee
    :clone :fee
    :meta {:description "Delivery fee"}}

   {:id :fees
    :clone :fee
    :meta {:description "Fees"}}

   {:id :charge
    :clone :fee
    :meta {:description "Charge"}}

   {:id :charges
    :clone :fee
    :meta {:description "Charges"}}

   {:id :manufacturer
    :type :document
    :required-un [:id :name :address :city :province :postal_code :country]
    :optional-un [:address2 :contact_email :phone]}

   {:id :sku
    :type :string
    :min 1
    :max 50
    :meta {:description "SKU"
           :sensible-values ["sku1" "sku2" "sku99"]}}

   {:id :manufacturer_id
    :clone :id
    :meta {:description "Manufacturer ID"
           :sensible-values ["man1" "man2" "man3" "man99"]}}

   {:id :product
    :type :document
    :required-un [:id :unit_cost]
    :optional-un [:sku :count :manufacturer_id]}

   {:id :product_id
    :clone :id
    :meta {:description "Product ID."}}

   {:id :line_item
    :type :document
    :required-un [:product_id :count :unit_cost]
    :optional-un [:amount :total :subtotal :charge :fee]}

   {:id :additional_charge
    :type :document
    :required-un []
    :optional-un []}

   {:id :line_items
    :type :vector
    :vector-of :line_item
    :meta {:description "Line items."
           :sensible-min 1
           :sensible-max 3}}

   {:id :customer
    :type :document
    :meta {:description "Customer"}
    :required-un [:id :first_name :last_name :address :province :city :country :province]
    :optional-un [:active :address2 :email :cell_phone :work_phone :home_phone]}

   {:id :customer_id
    :clone :id
    :meta {:description "Customer ID"}}

   {:id :order
    :type :document
    :required-un [:id :line_items :customer_id :total :subtotal :amount]
    :optional-un [:total :subtotal :taxes :fees :charges]}

   {:id :person
    :type :document
    :meta {:description "Person"}
    :required-un [:id :first_name :last_name :address :province :city :country :province]
    :optional-un [:active :address2 :email :cell_phone :work_phone :home_phone :long_lat :tags]}

   ])

(defn generate
  "Imports demo data elements and generated `demo.html` and `demo.md` documentation
  files in root folder."
  []
  (diction/imports! dictionary)
  (spit "demo.html" (doc/->html {:title "Demo Data Dictionary"}))
  (spit "demo.md" (doc/->markdown "Demo Data Dictionary")))

;; demo of custom data type

(defn generate-odd-pos-int
  "Generate a simple odd, positive integer."
  []
  (-> (rand)
      (* (/ Integer/MAX_VALUE 2))
      int
      (* 2)
      inc))

(defn validate-odd-pos-int
  "Validate a simple odd, positive integer."
  [v id entry]
  (when (not (and (int? v) (odd? v)))
    (let [fail {:id id :v v :entry entry}]
      (filter #(not (nil? %))
              [(when-not (int? v)
                 (assoc fail
                   :msg (str "Value '" v "' for field " id " needs to be an integer.")))
               (when-not (if (number? v)
                           (odd? (long v))
                           true)
                 (assoc fail
                   :msg (str "Value '" v "' for field " id " needs to be odd.")))]))))

(defn normalize-odd-pos-int
  "Normalizes the odd positive int number entry type elements (if necessary) given element map `m`.  If not a int number type, passhtru
  the element map `m`."
  [m]
  (if (= :odd-pos-int (:type m))
    (assoc m :gen-f (diction/wrap-gen-f generate-odd-pos-int)
             :valid-f validate-odd-pos-int)
    m))

(diction/type-normalizer! normalize-odd-pos-int)

(def odd-pos-int!
  "Programmatic custom registration function for demo custom data element."
  (partial diction/custom-element! :odd-pos-int))

(odd-pos-int! :field-of-odd-pos-int)
