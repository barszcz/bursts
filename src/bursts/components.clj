(ns bursts.components
  (:require [hiccup.core :refer [html]]
            [hiccup.def :refer [defhtml]]
            [hiccup.page :refer [html5]]))

(declare head highlight-js stylesheets)

(defn build-page [title content]
  (html5
   (head title)
   [:body
    (logo)
    content]))

(defn head [title]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1.0"}]
   [:title title]
   (stylesheets)
   (highlight-js)])

(defhtml logo []
  [:div.logo
   [:h2.typl8-gamma "Transient Random-noise " [:br] " Bursts With Announcements"]
   [:h3.typl8-delta "A blog by Jake Barszcz."]])

(defhtml stylesheets []
  [:link {:rel "stylesheet" :href "/tomorrow-night.css"}]
  [:link {:rel "stylesheet" :href "/normalize.css"}]
  [:link {:rel "stylesheet" :href "/typeplate.css"}]
  [:link {:rel "stylesheet" :href "/style.css"}])

(defhtml highlight-js []
  [:script {:src "/highlight.pack.js"}]
  [:script "hljs.initHighlightingOnLoad();"])
