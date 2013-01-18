(ns envision-cljs.core
  (:refer-clojure :exclude [remove]))


;; data format

;; We can simply use directly the envision data format,
;; but as vectors and smaps instead of array and json.

;; data -->
;; [[x1 x2 x3] [y1 y2 y3]] one time series

;; [ [[x1 x2 x3] [y1 y2 y3]]
;;   [[x1 x2 x3] [y1 y2 y3]] ] two time series

;; each series can be individually configured by transferring the data
;; into a map and adding the configuration:

;; [ [[x1 x2 x3] [y1 y2 y3]] 
;;   {:points {:show true}
;;    :data [[x1 x2 x3] [y1 y2 y3]]}]






;; ================
;; Methods wrapper
;; ================

;; component API


(defn new-component
  "Defines a visualization component.

Components are the building blocks of a visualization, representing
one typically graphical piece of the vis. This class manages the
options, DOM and API construction for an adapter which handles the
actual drawing of the visualization piece.

Adapters can take the form of an actual object, a constructor function
or a function returning an object. Only one of these will be used. If
none is submitted, the default adapter Flotr2 is used. Optional
configuration:
:name - A name for the component.
:element - A container element for the component.
:height - An explicit component height.
:width - An explicit component width.
:data - An array of data. Data may be formatted for envision or for the adapter itself, in which case skipPreprocess will also need to be submitted.
:skipPreprocess - Skip data preprocessing. This is useful when using the native data format for an adapter.
:adapter - An adapter object.
:adapterConstructor - An adapter constructor to be instantiated by the component.
:adapterCallback - An callback invoked by the component returning an adapter.
:config - Configuration for the adapter."
  [& [config]]
  (js/envision.Component. (clj->js config)))


(defn render
  "Render the component.

If no element is submitted, the component will
render in the element configured in the constructor.
------
Render the visualization.

If no element is submitted, the visualization will
render in the element configured in the constructor.

This method is chainable."
  [component element]
  (.render component element))

(defn draw
  "Draw the component."
  [component data options]
  (.draw component options))

(defn trigger
  "Trigger an event on the componentonent's API.

Arguments are passed through to the API."
  ([component arg]
     (.trigger component arg ))
  ([component arg arg2]
     (.trigger component arg (clj->js arg2))))

(defn attach
  "Attach to an event on the componentonent's API.

Arguments are passed through to the API."
  ([component arg]
     (.attach component arg))
  ([component arg arg2]
     (.attach component arg (clj->js arg2))))

(defn detach
  "Detach a listener from an event on the componentonent's API.

Arguments are passed through to the API."
  ([component arg]
     (.detach component arg))
  ([component arg arg2]
     (.detach component arg (clj->js arg2))))


(defn destroy
  "Destroy the component.

Empties the container and calls the destroy method on the
component's API.
----------
Destroys the visualization.

This empties the container and destroys all the components which are part
of the visualization."
  [vis]
  (.destroy vis))

(def quadratic-drawing js/envision.components.QuadraticDrawing)





;; visualization API


(defn new-visualization
  "Defines a visualization of componenents.

This class manages the rendering of a visualization. It provides
convenience methods for adding, removing, and reordered components
dynamically as well as convenience methods for working with a logical
group of components.

Optional configuration:
:name - A name for the visualization.
:element - A container element for the visualization."
  [& [config]]
  (js/envision.Visualization. (clj->js config)))

;; render --> see component API

(defn add
  "Add a component to the visualization.

If the visualization has already been rendered,
it will render the new component.

This method is chainable.
----------
Adds an action to the interaction."
  ([vis component]
     (let [c (if (map? component) (clj->js component) component)]
       (.add vis c)))
  ([vis-interaction component-action options]
     (let [ca (if (map? component-action) (clj->js component-action) component-action)]
       (.add vis-interaction ca (clj->js options)))))

(defn remove
  "Remove a component from the visualization.

This removes the components from the list of components in the
visualization and removes its container from the DOM.  It does not
destroy the component.

This method is chainable."
  [vis component]
  (.remove vis component))

(defn set-position
  "Reorders a component.

This method is chainable."
  [vis component new-index]
  (.setPosition vis component new-index))

(defn index-of
  "Gets the position of a component."
  [vis component]
  (.indexOf vis component))

(defn get-component
  "Gets the component at a position."
  [vis position]
  (.getComponent vis position))

(defn is-first
  "Gets whether or not the component is the first component
in the visualization."
  [vis component]
  (.isFirst vis component))

(defn is-last
  "Gets whether or not the component is the last component
in the visualization."
  [vis component]
  (.isLast vis component))

;; Destroy --> see component API








;; Preprocessor API

(defn new-preprocessor
  "Data preprocessor.

Data can be preprocessed before it is rendered by an adapter.

This has several important performance considerations. If data will be
rendered repeatedly or on slower browsers, it will be faster after
being optimized.

First, data outside the boundaries does not need to be rendered.
Second, the resolution of the data only needs to be at most the number
of pixels in the width of the visualization.

Performing these optimizations will limit memory overhead, important
for garbage collection and performance on old browsers, as well as
drawing overhead, important for mobile devices, old browsers and large
data sets.

Optional config map:
:data - The data for processing."
  [& [config]]
  (js/envision.Preprocessor. (clj->js config)))

(defn get-data
  "Returns data."
  [pproc]
  (.getData pproc))

(defn set-data
  "Set the data object."
  [pproc data]
  (.setData pproc data))

(defn length
  "Returns the length of the data set."
  [pproc]
  (.length pproc))

(defn bound
  "Bounds the data set at within a range."
  [pproc min max]
  (.bound pproc min max))

(defn subsample-min-max
  "Subsample data using MinMax.

MinMax will display the extrema of the subsample intervals.  This is
slower than regular interval subsampling but necessary for data that 
is very non-homogenous."
  [pproc resolution]
  (.subsampleMinMax pproc resolution))

(defn subsample
  "Subsample data at a regular interval for resolution.

This is the fastest subsampling and good for monotonic data and fairly
homogenous data (not a lot of up and down)."
  [pproc resolution]
  (.subsample pproc resolution))






;; Interaction API

(defn new-interaction
  "Defines an interaction between components.

This class defines interactions in which actions are triggered by
leader components and reacted to by follower components. These actions
are defined as configurable mappings of trigger events and event
consumers. It is up to the adapter to implement the triggers and
consumers.

A component may be both a leader and a follower. A leader which is a
follower will react to actions triggered by other leaders, but will
safely not react to its own. This allows for groups of components to
perform a common action.

Optionally, actions may be supplied with a callback executed before
the action is consumed. This allows for quick custom functionality to
be added and is how advanced data management (ie. live Ajax data) may
be implemented.

This class follow an observer mediator pattern.

Optional config map:
:leader - Component(s) to lead the interaction"
  [& [config]]
  (js/envision.Interaction. (clj->js config)))

(defn leader
  "Add a component as an interaction leader."
  [interaction component]
  (.leader interaction component))

(defn follower
  "Add a component as an interaction leader."
  [interaction component]
  (.follower interaction component))

(defn group
  "Adds an array of components as both followers and leaders."
  [interaction & components]
  (.group interaction (clj->js components)))

;; add --> see visualization API


(def default-selection-action
  {:events [{:handler "select", :consumer "zoom"}
            {:handler "click", :consumer "reset"}]})


;; ================
;; End of the wrapper section
;; ================


(defn recursive-merge
  "Recursively merge hash maps."
  [a b]
  (if (and (map? a) (map? b))
    (merge-with recursive-merge a b)
    b))


;; some defaults to avoid errors (ie: height of 0px)
(def defaults
  {:detail
   {:name "envision-timeseries-detail"
    :config {:lite-lines {:lineWidth 1, :show true}};; config to be passed to Flotr2 underlying library
    :height 300}
   :summary
   {:name "evision-timeseries-summay"
    :config {:lineWidth 1, :show true
             :handles {:show true}
             :selection {:mode :x}
             :yaxis {:autoscale true
                     :autoscaleMargin 0.1}}
    :height 70}
   :connection
   {:name "envision-timeseries-connection"
    :adapterConstructor quadratic-drawing ;; I don't even know if there's any other...
    }})
;; for the `config' parameter (Flotr2 config), see
;; http://www.humblesoftware.com/flotr2/documentation



;; implementation of a template timeSeries
(defn timeseries [smap]
  ;; var
   (let [merged-data (recursive-merge defaults smap)
         vis (new-visualization {:name "envision-timeseries"})
         selection (new-interaction)]
     
     ;; fill defaults
     ;; we already merged the smap, no need for Flotr merge function.
     
     ;; build components
     (let [detail (new-component (:detail merged-data))
           connection (new-component (:connection merged-data))
           summary (new-component (:summary merged-data))]
       (-> vis
           (add detail)
           (add connection)
           (add summary)
           (render (:container smap)))

      ;; selection action
      (follower selection detail)
      (follower selection connection)
      (leader selection summary)
      (add selection default-selection-action (if-let [c (:selectionCallback smap)] {:callback c}))

      ;; optional initial selection
      (when-let [s (:selection smap)]
        (trigger summary "select" s))

      ;; now set `this'. Is there a more clojurian way to do this?
      (this-as this       
               (set! (.-vis this) vis)
               (set! (.-selection this) selection)
               (set! (.-detail this) detail)
               (set! (.-summary this) summary)))))


;; That's it! Everything else is learning how to customize the graphs!




(defn add-demo-graph []
  (let [container (js/document.getElementById "graph")
        x-axis (range 500)
        y1-axis (map #(js/Math.sin (/ % 10)) x-axis)
        y2-axis (map #(js/Math.cos (/ % 50)) x-axis)
        serie-1 [x-axis, y1-axis]
        serie-2 {:points {:show true} ;; these config will be applied to each view (detail AND summary)
                 :shadowSize 4
                 :data [x-axis, y2-axis]}
        raw-data [serie-1, serie-2]
        options {:container container

                 ;; we can customize each plot individually
                 ;; let's start by the 'detail' view
                 :detail {:config {:lite-lines {:lineWidth 3
                                                :fill true
                                                :fillOpacity 0.2}
                                    :yaxis {:autoscale true
                                            :autoscaleMargin 0.05
                                            :tickFormatter #(str %)
                                            :noTicks 4
                                            :showLabels true
                                            :showMinorLabels true}
                                   ;; you could also customize yaxis2 (see Flotr for more details)
                                   :grid {:horizontalLines true
                                          :tickColor "black"
                                          :color "black"
                                          :labelMargin 3
                                          :outlineWidth 1
                                          :backgroundColor {:colors [[0,"#fff"], [1,"#ccc"]],
                                                            :start "top", :end "bottom"}
                                          ;;multiple properties (such
                                          ;;as backgroundColor) seems
                                          ;;to be broken when using
                                          ;;envision.min.js instead of
                                          ;;envirion.js.
                                          }
                                   :HtmlText true
                                   :xaxis {:mode "time" :labelsAngle 45}
                                   :mouse {:track true
                                           :trackAll true}}
                          :data raw-data}

                 ;; now the smaller 'summary' view
                 :summary {:data raw-data
                           :config {:xaxis {:tickFormatter #(str %)
                                            :noTicks 4,
                                            :showLabels true,
                                            ;; Here we don't autoscale
                                            ;; because we want it to
                                            ;; match the detailed
                                            ;; view.
                                            }
                                   :grid {:horizontalLines true
                                          :tickColor "black"
                                          :color "black"
                                          :labelMargin 3
                                          :outline "nsew"}
                                    :HtmlText true ;; seems to be
                                                   ;; necessary to
                                                   ;; show something.
                                                   ;; Another
                                                   ;; envision.min.js
                                                   ;; bug?
                                    }}
                 :selection {:data {:x {:min (* 0.1 (count x-axis))
                                        :max (* 0.3 (count x-axis))}}}}]

    (timeseries options) ;; DRAW IT!

    ;; a more more simple graph can be obtained like this:
    (comment
      (timeseries {:container container
                   :detail {:data raw-data}
                   :summary {:data raw-data}}))))