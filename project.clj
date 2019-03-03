(defproject diction "0.1.1"
  :description "Diction is a Clojure data dictionary with data shape definition/validation/generation with batteries included."
  :url "http://sierralogic.com/project/diction"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-tools-deps "0.4.3"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
