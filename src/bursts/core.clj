(ns bursts.core
  (:require [clojure.java.io :as io]
            [hiccup.page :refer [html5]]
            [stasis.core :as stasis]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [bursts.markdown :refer [md]])
  (:use [markdown.core]
        [markdown.transformers]
        [clojure.algo.generic.functor :only (fmap)]))

(defn layout-page [page]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title "Tech blog"]
    [:link {:rel "stylesheet" :href "/tomorrow-night.css"}]
    [:link {:rel "stylesheet" :href "/style.css"}]
    [:script {:src "/highlight.pack.js"}]
    [:script "hljs.initHighlightingOnLoad();"]]
   [:body
    [:div.logo
     [:h1 "Transient Random-noise Bursts With Announcements"]]
    page]))



(defn build-post [page]
  (let [[meta content] (str/split page #"-----")
        meta (edn/read-string meta)
        {:keys [title tags]} meta
        tags (map name tags)
        content (md content)]
    [:div.body
     [:h1.post-title title]
     [:div.content content]
     [:p "Tags: " (str/join " " tags)]]))

(defn build-posts [posts]
  (map (comp layout-page build-post) posts))

(defn post-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "/") (keys pages))
          (build-posts (vals pages))))

(defn partial-pages [pages]
  (zipmap (keys pages)
          (map layout-page (vals pages))))

(defn get-pages []
  (stasis/merge-page-sources
   {:public
    (stasis/slurp-directory "resources/public" #".*\.(html|css|js)$")
    :partials
    (partial-pages (stasis/slurp-directory "resources/partials" #".*\.html$"))
    :markdown
    (post-pages (stasis/slurp-directory "resources/md" #"\.md$"))}))

(def app (stasis/serve-pages get-pages))
