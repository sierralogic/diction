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

   ;; parent elements (need to go above child/clones
   {:id :moid
    :meta {:description "MongoDB string id"}
    :type :string
    :min 24
    :max 24
    :chars "abcdef0123456789"}

   ;; fields

   {:id :_id
    :meta {:description "MongoDB identifier"}
    :clone :moid}

   {:id :is_concierge
    :meta {:description "True if Peerspace add-on-item"}
    :type :boolean}

   {:id :peerspace_add_on
    :clone :is_concierge}

   {:id :active
    :meta {:description "Active flag (true/false)"}
    :type :boolean}

   {:id :additional_charges
    :meta {:description "Additional charges"
           :sensible-min 1
           :sensible-max 3}
    :type :vector
    :vector-of :item/additional_charge}

   {:id :address
    :meta {:description "Street address"
           :sensible-values [ "123 Main Street" "245 West 52nd" "1A Unicorn Way"]}
    :type :string
    :min 5
    :max 64}

   {:id :address2
    :meta {:description "Optional address line (used for units/numbers)"
           :sensible-values ["Unit 101" "Suite 2A" "#42"]}
    :type :string
    :min 0
    :max 64}

   {:id :amount
    :meta {:description "Amount in currency"
           :sensible-values [1.23 43.23 99.32]}
    :type :double}

   {:id :categories
    :meta {:description "Categories"
           :sensible-min 1
           :sensible-max 3}
    :type :vector
    :vector-of :name/category}

   {:id :category_id
    :meta {:description "Category id"}
    :clone :moid}

   {:id :category_ids
    :meta {:description "Category Ids"
           :sensible-min 1 :sensible-max 3}
    :type :vector
    :vector-of :category_id}

   {:id :name/category
    :meta {:description "Category"
           :sensible-values ["Photography" "Lunch" "Dinner" "Snack" "Breakfast"]}
    :type :string}

   {:id :charge_id
    :meta {:description "Charge Identifier"}
    :clone :moid}

   {:id :charge_type
    :meta {:description "Charge Type"}
    :type :enum
    :enum ["tax" "fee"]}

   {:id :charge_unit
    :meta {:description "Charge Unit ($/#)"}
    :type :enum
    :enum ["%" "$"]}

   {:id :city
    :meta {:description "City/village"
           :sensible-values ["New York" "Philadelphia" "San Francisco" "Virginia City"]}
    :type :string
    :min 0
    :max 64}

   {:id :contact
    :meta {:description "Contact info"
           :sensible-values ["Jane Doe" "Carlos Ruiz" "Thuyen Gnuyen"]}
    :type :string
    :min 2
    :max 100}

   {:id :country
    :meta {:description "Country/nation"
           :sensible-values ["US" "CA" "MX"]}
    :type :string}

   {:id :description
    :meta {:description "Description..."
           :sensible-values ["Something descriptive", "Another description", "This is descriptive"]}
    :type :string
    :min 0
    :max 100}

   {:id :ein
    :meta {:description "EIN IRS tax identifier"}
    :type :string
    :regex-pattern "\\d{2}-\\d{7}"}

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
           :sensible-values ["jane@acme.org" "mulan@cater.io"]}}

   {:id :notification_email
    :clone :email
    :meta {:description "Automated notification address"
           :sensible-values ["orders@acme.org" "fulfilment@cater.io"]}}

   {:id :notes
    :type :string
    :meta {:description "Notes"
           :sensible-values ["Some notes" "Notes are here" "And more notes"]}}

   {:id :rank
    :meta {:description "Sort ranking"
           :sensible-values [0 100 200 1000]}
    :type :long}

   {:id :image_id
    :meta {:description "Cloudinary image id"
           :sensible-values ["img123" "img_abc" "image-xyz"]}
    :type :string
    :min 4
    :max 8}

   {:id :image_ids
    :meta {:description "Vector of image ids."
           :sensible-min 1
           :sensible-max 3}
    :type :vector
    :vector-of :image_id}

   {:id :label
    :meta {:description "Label"
           :sensible-values ["Label1" "Label2" "Label 99"]}
    :type :string}

   {:id :latitude
    :meta {:description "Latitude geo"}
    :type :double
    :min -90.0
    :max 90.0}

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

   {:id :longitude
    :meta {:description "Longitude geo (-180 - 180)"}
    :type :double
    :min -180.0
    :max 180.0}

   {:id :metro
    :meta {:description "Metro areas"}
    :type :enum
    :enum ["LAMetro" "NYMetro" "SFBay"]}

   {:id :metros
    :meta {:description "Set of metro areas"
           :sensible-min 1
           :sensible-max 3}
    :type :set
    :set-of :metro}

   {:id :phone
    :meta {:description "Phone number"
           :sensible-values ["(555) 555-1212" "(213) 555-1761"]}
    :type :string
    :min 10
    :max 64}

   {:id :state
    :meta {:description "State/province"
           :sensible-values ["CA" "NY" "TX"]}
    :type :string
    :min 2
    :max 2}

   {:id :subcategory
    :meta {:description "Sub-Category"
           :sensible-values ["Subcat1" "Subcat2" "Subcat99"]}
    :type :string
    :min 0
    :max 64}

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

   {:id :tax_id
    :meta {:description "Tax Identifier"}
    :type :string
    :regex-pattern "\\d{2}-\\d{7}" }

   {:id :payment_id
    :meta {:description "Payment identifer"
           :payment_service "stripe"}
    :type :string}

   {:id :unit_cost
    :meta {:description "Cost per unit"
           :sensible-values [32.32 99.32 1.23]}
    :type :double
    :min 0}

   {:id :unit_cost_basis
    :meta {:description "Basis for unit cost"}
    :type :enum
    :enum ["per_person" "per_unit"]}

   {:id :use_type
    :meta {:description "Use type for items"}
    :type :enum
    :enum ["Production" "Off-Site" "Event"]}

   {:id :visible_on_listing
    :meta {:description "Will item be visible on the listing"}
    :type :boolean}

   {:id :zip
    :meta {:description "Zip/postal code"}
    :type :string
    :regex-pattern "\\d{5}"}

   {:id :geo/coordinates
    :meta {:description "List of geo polygons"}
    :type :vector
    :vector-of :geo/polygon
    :min 1
    :max 1}

   {:id :geo/polygon
    :meta {:description "List of lat/longs"}
    :type :vector
    :vector-of :long-lats
    :min 1
    :max 3}

   {:id :geo/type
    :meta {:description "Geo JSON type"}
    :type :enum
    :enum ["Multipolygon"]}

   {:id :item/vendors
    :meta {:description "List of vendors per item"
           :sensible-min 1
           :sensible-max 3}
    :type :vector
    :vector-of :item/vendor}

   {:id :region_id
    :meta {:description "Region id"
           :reference :region}
    :clone :moid}

   ;{:id :ref/region
   ; :clone :region_id}

   {:id :region_ids
    :meta {:description "List of region ids"
           :sensible-min 1
           :sensible-max 3}
    :type :vector
    :vector-of :region_id}

   ;{:id :ref/regions
   ; :clone :region_ids}

   {:id :vendor_id
    :meta {:description "Vendor id"
           :reference :vendor}
    :clone :moid}

   ;{:id :ref/vendor
   ; :clone :vendor_id}

   {:id :vendor_ids
    :meta {:description "List of vendor ids"
           :sensible-min 1
           :sensible-max 3}
    :type :vector
    :vector-of :vendor_id
    :min 0
    :max 20}

   ;{:id :ref/vendors
   ; :clone :vendor_ids}

   {:id :vendor/label
    :meta {:description "Vendor name/label"
           :sensible-values ["Acme Hospitality" "Bars and Bells" "Frida's Good Eats"]}
    :type :string
    :min 2
    :max 64}

   ;{:id :x
   ; :meta {:description ""
   ;        :sensible-values nil}
   ; :type :x}

   ;; documents

   {:id :label/category
    :meta {:description "Category string label"
           :sensible-values ["Category #1" "Category A" "Category Z"]}
    :type :string}

   {:id :label/subcategory
    :meta {:description "Subcategory string label"
           :sensible-values ["Subcategory #1" "Subcategory A" "Subcategory Z"]}
    :type :string}

   {:id :item/additional_charge
    :meta {:description "Additional charge"}
    :type :document
    :required-un [:label :amount :charge_type :charge_unit :charge_id]}

   {:id :region/area
    :meta {:description "Geo JSON area"}
    :type :document
    :required-un [:geo/type :geo/coordinates]}

   {:id :category_subcategory
    :type :document
    :required-un [:label :name/category :subcategory :rank]}

   {:id :category_subcategories
    :meta {:description "Catego"}
    :type :vector
    :vector :category_subcategory}

   {:id :item
    :meta {:description "Inventory item"}
    :type :document
    :required-un [:_id :active :name/category :label :subcategory :unit_cost
                  :unit_cost_basis :visible_on_listing]
    :optional-un [:additional_charges :category_ids :category_subcategory
                  :description :image_ids :item/region_vendors :is_concierge
                  :metro :notes :peerspace_add_on :region_ids :tags :use_types]}

   {:id :region
    :meta {:description "Region with geo JSON polygon"}
    :type :document
    :required-un [:_id :active :region/area :description :metro :label]
    :optional-un [:tags]}

   {:id :vendor
    :meta {:description "Vendors information"}
    :type :document
    :required-un [:_id :vendor/label :active :description :contact :address :city
                  :state :zip :contact_email :phone]
    :optional-un [:address2 :country :metros :payment_id
                  :notification_email :tags :notes :image_ids]}

   {:id :item/region_vendor
    :meta {:description "Item region vendor association internal nested in item"}
    :type :document
    :required-un [:active :region_id :vendor_ids]
    :optional-un [:additional_charges :unit_cost :unit_cost_basis]}

   {:id :item/region_vendors
    :meta {:description "Region vendors internal to Item."
           :sensible-min 1
           :sensible-max 3}
    :type :vector
    :vector-of :item/region_vendor}

   {:id :category
    :type :document
    :required-un [:_id :active :label/category :label :label/subcategory]
    :optional-un [:notes :rank :tags]}

   ;{:id :x
   ; :meta {:label ""
   ;        :description ""
   ;        :sensible-values nil}
   ; :type :document
   ; :required-un []
   ; :optional-un []}

   ]

  )
