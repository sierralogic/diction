(ns diction.demo
  (:require [diction.core :refer [clone! enum! generate string! long! set-of! pos-double! double! boolean! vector! document!
                                  poly-vector! tuple!] :as dict]
            [diction.documentation :as doc]))


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

(string! :moid {:min 24 :max 24 :chars "abcdef0123456789" :meta {:hint "24-char MongoDB string id"}})

(clone! :moid :_id)

(string! :label {:meta {:sensible-values ["label1" "label2" "label zed"]}})
(string! :description {:meta {:sensible-values ["Something descriptive" "Another description" "This is descriptive"]}})

(boolean! :active)

(string! :category {:meta {:sensible-values ["cata" "catb" "catz"]}})

(string! :subcategory {:meta {:sensible-values ["subcat1" "subcat2" "subcat99"]}})

(vector! :categories :category)

(boolean! :visible_on_listing)

(pos-double! :unit_cost {:meta {:sensible-values [1.23 6.99 1443.32]}})
(enum! :unit_cost_basis ["per_person" "per_unit"] {:meta {:default "stuff"}})

(string! :image_id {:min 4 :max 8})
(vector! :image_ids :image_id {:min 1 :max 1})

(pos-double! :amount)

(clone! :moid :charge_id)
(enum! :charge_type ["tax" "fee"])
(enum! :charge_unit ["%" "$"] {:default "%"})

(clone! :moid :ref/vendor {:meta {:reference :vendor}})
(vector! :ref/vendors :ref/vendor {:meta {:reference :vendor}})

(clone! :moid :ref/region {:meta {:reference :region}})

(vector! :ref/regions :ref/region {:meta {:reference :region}})

(document! :additional_charge
           [:label :amount :charge_type :charge_unit :charge_id])

(vector! :additional_charges :additional_charge {:max 5})

(string! :tag {:min 4 :max 12})
(vector! :tags :tag {:min 0 :max 8})

(enum! :metro ["SFBay" "NYMetro" "LAMetro"])

(set-of! :metros :metro)

(document! :item/vendor
           [:ref/vendor :ref/region]
           [:unit_cost :unit_cost_basis :additional_charges])

(vector! :item/vendors :item/vendor)

(enum! :use_type ["Production" "Off-Site" "Event"])

(document! :item
           [:_id :label :category :subcategory :active
            :visible_on_listing :unit_cost :unit_cost_basis]
           [:image_ids :additional_charges :metro :use_type :tags
            :ref/regions
            :item/vendors])

;; regions
;; _id : string; human-readable; like brook or east_sf_bay
;active : true | false
;area : document; geojson / MultiPolygon
;description : string
;label : string (AT: name)
;tags : list of strings
;metro : string; associated metro_id

(double! :latitude {:min -90.0 :max 90.0})
(double! :longitude {:min -180.0 :max 180.0})

(enum! :geo/type ["Multipolygon"])

(tuple! :long-lat [:longitude :latitude])

(vector! :long-lats :long-lat {:min 3 :max 5})

(vector! :geo/polygon :long-lats {:min 1 :max 1})

(vector! :geo/coordinates :geo/polygon)

(document! :area
           [:geo/type :geo/coordinates])

(document! :region
           [:_id :label :active :area
            :metro :description]
           [:tags])

(string! :contact {:min 2 :max 30 :meta {:sensible-values ["Jane Doe" "Carlos Ruis"]}})
(string! :address {:min 5 :meta {:sensible-values ["123 Main St." "245 W. 52nd" "1A Unicorn Way"]}})
(string! :address2 {:meta {:sensible-values ["Unit 101" "Suite 2A" "#42"]}})
(string! :city {:meta {:sensible-values ["San Francisco" "Brooklyn" "Austin" "Burbank"]}})
(string! :state {:min 2 :max 2 :meta {:label "State" :description "State or province" :hint "Enter state/province" :sensible-values ["CA" "NY" "TX"]}})
(string! :country {:meta {:sensible-values ["US"]}})
(string! :zip {:regex-pattern "\\d{5}"})
(string! :ein {:regex-pattern "\\d{2}-\\d{7}"})
(clone! :ein :tax_id)
(string! :email {:regex-pattern ".+@.+\\..{2,20}" :meta {:sensible-values ["jane@acme.org" "mulan@cater.io"]}})
(string! :phone {:min 10 :meta {:sensible-values ["(555) 555-1212" "(213) 555-1761"]}})

(string! :vendor/name {:min 2 :max 30 :meta {:sensible-values ["Acme Hospitality" "Bars and Bells" "Frida's Good Eats"]}})
(clone! :area :service_area)
(document! :vendor
           [:_id :vendor/name :active :description
            :contact :address :city :state :zip :email :phone]
           [:address2 :service_area :country :metros :tax_id])
