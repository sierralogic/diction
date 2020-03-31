(defproject diction "0.2.1"
  :description "Diction is a Clojure data dictionary with data shape definition/validation/generation with batteries included."
  :url "http://sierralogic.com/project/diction"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [cheshire "5.10.0"]
                 [clj-time "0.15.0"]
                 [com.velisco/strgen "0.1.8"]
                 [hiccup "1.0.5"]
                 [hiccup-bridge "1.0.1"]
                 [markdown-clj "1.10.2"]]
  :repositories [["github" {:url "https://maven.pkg.github.com/sierralogic/gpr"
                            :username [:gpg :env/github_username]
                            :password [:gpg :env/github_password]}]]
  :plugins [[lein-codox "0.10.7"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
