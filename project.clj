(defproject diction "0.2.3"
  :description "Diction is a Clojure data dictionary with data shape definition/validation/generation with batteries included."
  :url "https://sierralogic.github.io/diction"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [cheshire "5.12.0"]
                 [clj-time "0.15.2"]
                 [com.velisco/strgen "0.2.5"]
                 [hiccup "1.0.5"]
                 [hiccup-bridge "1.0.1"]
                 [markdown-clj "1.11.9"]]
  :repositories [["github" {:url "https://maven.pkg.github.com/sierralogic/gpr"
                            :username [:gpg :env/github_username]
                            :password [:gpg :env/github_password]}]
                 ["clojars" {:url "https://clojars.org/repo"
                             :username :env/clojars_username
                             :password :env/clojars_password
                             :sign-releases false}]]
  :plugins [[lein-codox "0.10.7"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
