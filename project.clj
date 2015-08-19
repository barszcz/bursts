(defproject bursts "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[endophile "0.1.2"]
                 [org.clojure/clojure "1.6.0"]
                 [ring "1.3.2"]
                 [stasis "2.2.2"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.65"]
                 [org.clojure/algo.generic "0.1.2"]
                 [optimus "0.17.1"]
                 [optimus-sass "0.0.3"]]
  :ring {:handler bursts.core/app}
  :profiles {:dev {:plugins [[lein-ring "0.9.3"]]}})
