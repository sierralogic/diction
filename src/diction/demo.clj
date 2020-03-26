(ns diction.demo
  (:require [diction.core :refer [clone!
                                  generate info data-dictionary explain
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

   {:id :label
    :meta {:description "Name or label"
           :sensible-values ["Name #1" "Name A" "Name as such"]}
    :type :string
    :min 1
    :max 100}

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

   {:id :long-lat
    :meta {:description "Longitude and latitude pair"}
    :type :tuple
    :tuple [:longitude :latitude]}

   {:id :long-lats
    :meta {:description "List of longitude/latitude pairs."
           :sensible-min 1
           :sensible-max 1}
    :type :vector
    :vector-of :long-lat
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
    :meta {:description "Sales Tax"
           :label "Sales Tax"}}

   {:id :taxes
    :clone :tax
    :meta {:description "Taxes"
           :label "Taxes"}}

   {:id :fee
    :type :double
    :meta {:description "Fee."
           :sensible-values [3.23 0.34 99.99 42.42 1.23]}}

   {:id :delivery_fee
    :clone :fee
    :meta {:description "Delivery fee"
           :label "Delivery Fee"}}

   {:id :fees
    :clone :fee
    :meta {:description "Fees"
           :label "Fees"}}

   {:id :charge
    :clone :fee
    :meta {:description "Charge"
           :label "Charge"}}

   {:id :charges
    :clone :fee
    :meta {:description "Charges"
           :label "Charges"}}

   {:id :manufacturer
    :type :document
    :required-un [:id]}

   {:id :sku
    :type :string
    :min 1
    :max 50
    :meta {:description "SKU"
           :label "SKU"
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

   {:id :order
    :type :document
    :required-un [:id :line_items :total :subtotal :amount]
    :optional-un [:total :subtotal :taxes :fees :charges]}

   ])

;; (imports! dictionary) ;; imports the dictionary elements
;; (spit "dictionary.html" (doc/->html))  ;; writes out the data dictional nav HTML page

