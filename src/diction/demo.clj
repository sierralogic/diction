(ns diction.demo
  (:require [diction.core :refer [clone!
                                  generate info data-dictionary explain
                                  long!
                                  pos-double! neg-double! double!
                                  pos-float! neg-float! float!
                                  pos-int! neg-int! int!
                                  string!
                                  boolean!
                                  vector! set-of! tuple! poly-vector! enum!
                                  document!] :as diction]
            [diction.documentation :as doc]
            [diction.util :as util]))


;;x_id : MongoDB OID unique id
;xactive : true|false

;additional_charges : list of addt'l charges
  ;amount : decimal
  ;charge_id : string/oid; generated but not used outside of item
  ;charge_type : string; tax | fee
  ;charge_unit : string; % | $
  ;label : string; short description of addt'l charge

;group_id : string
;min_count : integer; minimal count required to purchase
;space_id : string; MongoDB oid; item associated to space
;sso_id : string; MongoDB sso id of owner/vendor; all one's 111...111 for Peerspace add-ons/concierge
;is_concierge : true|false; Peerspace concierge convenience flag


;xcategory : string
;xdescription : string
;ximage_ids : list of strings; cloudinary image ids
;xlabel : string; (AT: name)
;xsubcategory : string
;xunit_cost : decimal; cost per unit
;xunit_cost_basis : string; per_person | per_unit | ?
;xvisible_on_listing : true|false

;Example

;(string! :moid {:min 24 :max 24 :chars "abcdef0123456789" :meta {:hint "24-char MongoDB string id"}})
;
;(clone! :moid :_id)
;
;(string! :label {:meta {:sensible-values ["label1" "label2" "label zed"]}})
;(string! :description {:meta {:sensible-values ["Something descriptive" "Another description" "This is descriptive"]}})
;
;(boolean! :active)
;
;(string! :category {:meta {:sensible-values ["cata" "catb" "catz"]}})
;
;(string! :subcategory {:meta {:sensible-values ["subcat1" "subcat2" "subcat99"]}})
;
;(vector! :categories :category)
;
;(boolean! :visible_on_listing)
;
;(pos-double! :unit_cost {:meta {:sensible-values [1.23 6.99 1443.32]}})
;(enum! :unit_cost_basis ["per_person" "per_unit"] {:meta {:default "stuff"}})
;
;(string! :image_id {:min 4 :max 8})
;(vector! :image_ids :image_id {:min 1 :max 1})
;
;(pos-double! :amount)
;
;(clone! :moid :charge_id)
;(enum! :charge_type ["tax" "fee"])
;(enum! :charge_unit ["%" "$"] {:default "%"})
;
;(clone! :moid :ref/vendor {:meta {:reference :vendor}})
;(vector! :ref/vendors :ref/vendor {:meta {:reference :vendor}})
;
;(clone! :moid :ref/region {:meta {:reference :region}})
;
;(vector! :ref/regions :ref/region {:meta {:reference :region}})
;
;(document! :additional_charge
;           [:label :amount :charge_type :charge_unit :charge_id])
;
;(vector! :additional_charges :additional_charge {:max 5})
;
;(string! :tag {:min 4 :max 12})
;(vector! :tags :tag {:min 0 :max 8})
;
;(enum! :metro ["SFBay" "NYMetro" "LAMetro"])
;
;(set-of! :metros :metro)
;
;(document! :item/vendor
;           [:ref/vendor :ref/region]
;           [:unit_cost :unit_cost_basis :additional_charges])
;
;(vector! :item/vendors :item/vendor)
;
;(enum! :use_type ["Production" "Off-Site" "Event"])
;
;(document! :item
;           [:_id :label :category :subcategory :active
;            :visible_on_listing :unit_cost :unit_cost_basis]
;           [:image_ids :additional_charges :metro :use_type :tags
;            :ref/regions
;            :item/vendors])
;
;(double! :latitude {:min -90.0 :max 90.0})
;(double! :longitude {:min -180.0 :max 180.0})
;
;(enum! :geo/type ["Multipolygon"])
;
;(tuple! :long-lat [:longitude :latitude])
;
;(vector! :long-lats :long-lat {:min 3 :max 5})
;
;(vector! :geo/polygon :long-lats {:min 1 :max 1})
;
;(vector! :geo/coordinates :geo/polygon)
;
;(document! :area
;           [:geo/type :geo/coordinates])
;
;(document! :region
;           [:_id :label :active :area
;            :metro :description]
;           [:tags])
;
;(string! :contact {:min 2 :max 30 :meta {:sensible-values ["Jane Doe" "Carlos Ruis"]}})
;(string! :address {:min 5 :meta {:sensible-values ["123 Main St." "245 W. 52nd" "1A Unicorn Way"]}})
;(string! :address2 {:meta {:sensible-values ["Unit 101" "Suite 2A" "#42"]}})
;(string! :city {:meta {:sensible-values ["San Francisco" "Brooklyn" "Austin" "Burbank"]}})
;(string! :state {:min 2 :max 2 :meta {:label "State" :description "State or province" :hint "Enter state/province" :sensible-values ["CA" "NY" "TX"]}})
;(string! :country {:meta {:sensible-values ["US"]}})
;(string! :zip {:regex-pattern "\\d{5}"})
;(string! :ein {:regex-pattern "\\d{2}-\\d{7}"})
;(clone! :ein :tax_id)
;(string! :email {:regex-pattern ".+@.+\\..{2,20}" :meta {:sensible-values ["jane@acme.org" "mulan@cater.io"]}})
;(string! :phone {:min 10 :meta {:sensible-values ["(555) 555-1212" "(213) 555-1761"]}})
;
;(string! :vendor/name {:min 2 :max 30 :meta {:sensible-values ["Acme Hospitality" "Bars and Bells" "Frida's Good Eats"]}})
;(clone! :area :service_area)
;(document! :vendor
;           [:_id :vendor/name :active :description
;            :contact :address :city :state :zip :email :phone]
;           [:address2 :service_area :country :metros :tax_id])

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

   {:id :active
    :meta {:description "Active flag (true/false)"}
    :type :boolean}

   {:id :additional_charges
    :meta {:description "Additional charges"}
    :type :vector
    :vector-of :additional_charge}

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
    :meta {:description "Categories"}
    :type :vector
    :vector-of :category}

   {:id :category
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
           :sensible-values ["New York" "Philadelphia" "San Francisco"]}
    :type :string
    :min 0
    :max 64}

   {:id :contact
    :meta {:description "Contact info"
           :sensible-values ["Jane Doe" "Carlos Ruiz"]}
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

   {:id :image_id
    :meta {:description "Cloudinary image id"
           :sensible-values nil}
    :type :string
    :min 4
    :max 8}

   {:id :image_ids
    :meta {:description "Vector of image ids."}
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
    :meta {:description "List of longitude/latitude pairs."}
    :type :vector
    :vector-of :long-lat
    :min 1
    :max 3}

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
    :meta {:description "Set of metro areas"}
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
           :sensible-values ["tag1" "tag2" "tag99"]}
    :type :string
    :min 2
    :max 32}

   {:id :tags
    :meta {:description "Smart tags"}
    :type :set
    :set-of :tag
    :min 0
    :max 12}

   {:id :tax_id
    :meta {:description "Tax Identifier"}
    :type :string
    :regex-pattern "\\d{2}-\\d{7}" }

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
    :meta {:description "List of vendors per item"}
    :type :vector
    :vector-of :item/vendor}

   {:id :ref/region
    :meta {:description "Region id"
           :reference :region}
    :clone :moid}

   {:id :ref/regions
    :meta {:description "List of region ids"}
    :type :vector
    :vector-of :ref/region}

   {:id :ref/vendor
    :meta {:description "Vendor id"
           :reference :vendor}
    :clone :moid}

   {:id :ref/vendors
    :meta {:description "List of vendor ids"}
    :type :vector
    :vector-of :ref/vendor
    :min 0
    :max 3}

   {:id :vendor/name
    :meta {:description "Vendor name"
           :sensible-values ["Acme Hospitality" "Bars and Bells" "Frida's Good Eats"]}
    :type :string
    :min 2
    :max 64}

   ;{:id :x
   ; :meta {:description ""
   ;        :sensible-values nil}
   ; :type :x}

   ;; documents

   {:id :additional_charge
    :meta {:description ""}
    :type :document
    :required-un [:label :amount :charge_type :charge_unit :charge_id]}

   {:id :area
    :meta {:description "Geo JSON area"}
    :type :document
    :required-un [:geo/type :geo/coordinates]}

   {:id :item
    :meta {:description "Inventory item"}
    :type :document
    :required-un [:_id :label :category :subcategory :active :visible_on_listing :unit_cost :unit_cost_basis]
    :optional-un [:image_ids :additional_charges :metro :use_type :tags :ref/regions :item/vendors]}

   {:id :region
    :meta {:description "Region with geo JSON polygon"}
    :type :document
    :required-un [:_id :label :active :area :metro :description]
    :optional-un [:tags]}

   {:id :service_area
    :meta {:description "Service area as geo JSON polygon"}
    :type :document
    :required-un [:geo/type :geo/coordinates]}

   {:id :vendor
    :meta {:description "Vendors information"}
    :type :document
    :required-un [:_id :vendor/name :active :description :contact :address :city
                  :state :zip :email :phone]
    :optional-un [:address2 :service_area :country :metros :tax_id]}

   {:id :item/vendor
    :meta {:label "Vendor (internal to Item)"
           :description "Vendor ID"}
    :type :document
    :required-un [:ref/vendor :ref/region]
    :optional-un [:unit_cost :unit_cost_basis :additional_charges]}

   ;{:id :x
   ; :meta {:label ""
   ;        :description ""
   ;        :sensible-values nil}
   ; :type :document
   ; :required-un []
   ; :optional-un []}

   ]

  )

(defn clean
  [elem]
  (dissoc elem :type :id))

(defn import-document!
  [elem]
  (let [{:keys [id required required-un optional optional-un]} elem]
    (document! id required required-un optional optional-un (clean elem) nil)))

(defn import-clone!
  [elem]
  (clone! (:clone elem) (:id elem) (clean elem)))

(defn import-element!
  ([elem] (import-element! string! elem))
  ([diction-f elem]
   (diction-f (:id elem) elem)))

(defn import-ofs!
  [register-f of-key elem]
  (let [{:keys [id]} elem
        of (get elem of-key)]
    (register-f id of (clean elem))))

(def import-vector! (partial import-ofs! vector! :vector-of))
(def import-poly-vector! (partial import-ofs! poly-vector! :poly-vector-of))
(def import-set! (partial import-ofs! set-of! :set-of))
(def import-enum! (partial import-ofs! enum! :enum))
(def import-tuple! (partial import-ofs! tuple! :tuple))

(def import-string! (partial import-element! string!))
(def import-double! (partial import-element! double!))
(def import-pos-double! (partial import-element! pos-double!))
(def import-neg-double! (partial import-element! neg-double!))
(def import-long! (partial import-element! long!))
(def import-float! (partial import-element! float!))
(def import-pos-float! (partial import-element! pos-float!))
(def import-neg-float! (partial import-element! neg-float!))
(def import-boolean! (partial import-element! boolean!))

(def types->functions
  {:string import-string!
   :document import-document!
   :double import-double!
   :pos-double import-pos-double!
   :neg-double import-neg-double!
   :long import-long!
   :float import-float!
   :pos-float import-pos-float!
   :neg-float import-neg-float!
   :vector import-vector!
   :set import-set!
   :enum import-enum!
   :tuple import-tuple!
   :poly-vector import-poly-vector!
   :boolean import-boolean!
   :clone import-clone!})

(defn register-element!
  "Register element `elem` using declarative data."
  [elem]
  (let [nelem (if (:clone elem)
                (assoc elem :type :clone)
                elem)
        type (:type nelem :string)
        register-f (get types->functions type import-string!)]
    (register-f elem)))

(defn import!
  [elem]
  (register-element! elem))

(defn imports!
  [elements]
  (let [res (reduce #(conj (or % []) (import! %2))
                    nil
                    elements)]

    res))

(register-element! {:id :answer :type :long :min 40 :max 63 :meta {:description "The answers are out there."}})
