<template>
  <div>
    <MglMap
      :style="`width: ${this.width}; height: ${this.height}; z-index=auto`"
      :accessToken="accessToken"
      :mapStyle.sync="mapStyle"
      @load="onMapLoad"
      @click="onClickMap"
    >
      <MglGeocoderControl
        position="top-right"
        v-if="mode == 'showAll' || mode == 'create'"
        :accessToken="accessToken"
        :input.sync="defaultInput"
        @results="handleSearch"
      />
      <MglNavigationControl position="top-right" />
      <MglGeolocateControl position="top-right" v-if="mode == 'showAll'" @geolocate="geoLocate" />

      <v-container v-if="mode != 'create' || isMarkerSet == true">
        <MglMarker
          v-for="location in locations"
          :key="location.id"
          :coordinates="[location.koordinaten.Y, location.koordinaten.X]"
          :draggable="mode === 'update'"
          color="blue"
          @dragend="dragend"
        >
          <MglPopup v-if="mode === 'showAll'">
            <v-card flat elevation="0">
              <v-img
                class="white--text align-end"
                gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                height="200px"
                :src="'https://picsum.photos/510/300?random'"
                aspect-ratio="2"
              >
                <v-card-title>{{ location.bezeichnung }}</v-card-title>
              </v-img>
              <v-card-actions>
                <v-btn
                  :to="{ name: 'Location anzeigen', params: { id: location.id }}"
                  text
                >Mehr Details</v-btn>
              </v-card-actions>
            </v-card>
          </MglPopup>
        </MglMarker>
      </v-container>
    </MglMap>
  </div>
</template>

<script>
import Mapbox from "mapbox-gl";
import MglGeocoderControl from "vue-mapbox-geocoder";
import {
  MglMap,
  MglNavigationControl,
  MglGeolocateControl,
  MglMarker,
  MglPopup
} from "vue-mapbox";

export default {
  components: {
    MglMap,
    MglNavigationControl,
    MglGeolocateControl,
    MglMarker,
    MglPopup,
    MglGeocoderControl
  },
  data() {
    return {
      accessToken:
        "pk.eyJ1IjoibWtsZWluZWdnZXIiLCJhIjoiY2syeDB1bXNuMDc3ZzNndGFvMnhhNDB0eSJ9.g36eaDLBy327_G9xTFVWKQ",
      mapStyle: "mapbox://styles/mapbox/streets-v11",
      defaultInput: "",
      valid: true,
      isMarkerSet: false,
      mapbox: null
    };
  },
  props: {
    width: String,
    height: String,
    locations: Array,
    mode: String,
    center: Array
  },
  created() {
    this.mapbox = Mapbox;
  },
  methods: {
    async onMapLoad(event) {
      const asyncActions = event.component.actions;

      const newParams = await asyncActions.flyTo({
        center: this.center,
        zoom: 12,
        speed: 1
      });
    },
    handleSearch(event) {
      //for the future, search for locations nearby
    },
    validateForCreate() {
      //check if the Marker has been set, because when the marker has been set
      //you know that the map has now a marker by the create so it is OK.
      this.valid = this.isMarkerSet == true ? true : false;
    },
    onClickMap(event) {
      if (this.mode === "create") {
        this.isMarkerSet = true;

        //set the coords for Mapbox Marker with the Obj X/Y
        this.locations[0].koordinaten.X = event.mapboxEvent.lngLat.lat;
        this.locations[0].koordinaten.Y = event.mapboxEvent.lngLat.lng;
        //set the coords for the Webservice because it only takes x/y for update
        this.locations[0].koordinaten.x = event.mapboxEvent.lngLat.lat;
        this.locations[0].koordinaten.y = event.mapboxEvent.lngLat.lng;
      }
    },
    dragend(event) {
      if (this.mode === "update") {
        //set the coords for Mapbox Marker with the Obj X/Y
        this.locations[0].koordinaten.X = event.marker._lngLat.lat;
        this.locations[0].koordinaten.Y = event.marker._lngLat.lng;
        //set the coords for the Webservice because it only takes x/y for update
        this.locations[0].koordinaten.x = event.marker._lngLat.lat;
        this.locations[0].koordinaten.y = event.marker._lngLat.lng;
      }
    },
    geoLocate(event) {
      if (event.map.getSource("polygon")) {
        event.map.removeLayer("polygon");
        event.map.removeSource("polygon");
      }

      event.map.addSource(
        "polygon",
        this.createGeoJSONCircle(event.mapboxEvent.coords, 3)
      );

      event.map.addLayer({
        id: "polygon",
        type: "fill",
        source: "polygon",
        layout: {},
        paint: {
          "fill-color": "blue",
          "fill-opacity": 0.4
        }
      });
    },
    createGeoJSONCircle(center, radiusInKm, points) {
      if (!points) points = 64;

      var coords = {
        latitude: center.latitude,
        longitude: center.longitude
      };

      var km = radiusInKm;

      var ret = [];
      var distanceX =
        km / (111.32 * Math.cos((coords.latitude * Math.PI) / 180));
      var distanceY = km / 110.574;

      var theta, x, y;
      for (var i = 0; i < points; i++) {
        theta = (i / points) * (2 * Math.PI);
        x = distanceX * Math.cos(theta);
        y = distanceY * Math.sin(theta);

        ret.push([coords.longitude + x, coords.latitude + y]);
      }
      ret.push(ret[0]);

      return {
        type: "geojson",
        data: {
          type: "FeatureCollection",
          features: [
            {
              type: "Feature",
              geometry: {
                type: "Polygon",
                coordinates: [ret]
              }
            }
          ]
        }
      };
    }
  }
};
</script>

<style>
.mapboxgl-popup {
  min-width: 300px;
  min-height: 500px;
}
.mapboxgl-popup-anchor-left {
  margin-left: 10px;
}
.mapboxgl-popup-anchor-right {
  margin-left: -10px;
}
.mapboxgl-popup-content {
  padding: 0%;
}
.mapboxgl-popup-close-button {
  display: none;
}
</style>