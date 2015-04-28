(ns bursts.markdown
  (:require [hiccup.core :refer [html]]
            [clojure.string :as str]
            [markdown.core :refer [md-to-html-string]]
            [markdown.transformers :refer [transformer-vector]]))

(defn footnote [text {:keys [code codeblock] :as state}]
  [(if (or code codeblock)
     text
     (if-let [[_ reference note :as matches] (re-matches #"^\[\^(.+)\]\: (.*)$" text)]
       (html
         [:p
          [:a {:name reference}]
          [:sup "&#91;" reference "&#93; "]
          note
          [:a {:href (str "#ref-" reference)} "&#8617;"]])
       text))
   state])

(defn footnote-ref [text state]
  [(if (:code state)
     text
     (str/replace
      text
      #"\[\^.+\]"
      #(let [reference (subs % 2 (dec (count %)))]
         (html
          [:sup
           [:a {:name (str "ref-" reference)
                :href (str "#" reference)}
            "[" reference "]"]]))))
   state])

(def transformers
  (cons footnote (cons footnote-ref transformer-vector)))

(defn md [content]
  (md-to-html-string content :replacement-transformers transformers))
