(defproject org.clojars.frozenlock/envision-cljs "0.1.3"
  :description "Wrapper for the Envision.js library (Graphs!)"
  :url "https://github.com/Frozenlock/envision-cljs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :plugins [[lein-cljsbuild "0.2.10"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds {:main {:jar true
                              :source-path "src-cljs"
                              :compiler {:output-to "resources/public/js/cljs.js"
                                         :externs ["src-cljs/externs/externs.js"]}}}})
  