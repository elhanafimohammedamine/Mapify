
const map = L.map("map").setView([33.9735883, -6.9381193], 10);
const initialLayer = L.tileLayer('https://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
    maxZoom: 20,
    subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
}).addTo(map);

const layers =  {
    defaultLayer : {
        url : "https://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}",
        accessToken: null,
        maxZoom: 20,
        subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
    },
    lightLayer : {
        url : "https://{s}.google.com/vt/lyrs=s,h&x={x}&y={y}&z={z}",
        accessToken: null,
        maxZoom: 20,
        subdomains: ['mt0','mt1','mt2','mt3']
    },
    satelliteLayer : {
        url : "https://{s}.google.com/vt/lyrs=s,h&x={x}&y={y}&z={z}",
        accessToken : null,
        maxZoom: 20,
        subdomains: ['mt0','mt1','mt2','mt3']
    }
}
const positionIcon = L.icon({
    iconUrl: './assets/images/posIcon.png',
    iconSize: [40, 40],
    iconAnchor: [22, 22],
    popupAnchor: [-3, -76],
    shadowUrl: './assets/images/posShadow.png',
    shadowSize: [40, 40],
    shadowAnchor: [22, 22],
});
const userIcon = L.icon({
    iconUrl: './assets/images/userIcon.png',
    iconSize: [40, 40],
    iconAnchor: [22, 22],
    popupAnchor: [-3, -76],
    shadowUrl: './assets/images/posShadow.png',
    shadowSize: [40, 40],
    shadowAnchor: [22, 22],
});
let circle = L.circle([null, null], {
    color: '#4025d6',
    fillColor: '#5c3eff',
    fillOpacity: 0.5,
    radius: null
})
let userLocation = {
    latitude: null,
    longitude: null
}

// functions definitions
function initiateMapSetup(){
    map.zoomControl.remove();
}
function setMapLayer(mapLayer) {
    if(mapLayer.url !== "") {
        const newLayer = L.tileLayer(mapLayer.url, {subdomains: mapLayer.subdomains, maxZoom: mapLayer.maxZoom});
        if (!map.hasLayer(newLayer)) {
            map.removeLayer(newLayer);
            newLayer.addTo(map);
            return "yes"
        }
    }
    return "no"
}
function mapLayerChooser(layer) {
    switch (layer) {
        case "defaultLayer":
            setMapLayer(layers.defaultLayer)
            break
        case "lightLayer":
            setMapLayer(layers.lightLayer)
            break
        case "satelliteLayer":
            setMapLayer(layers.satelliteLayer)
            break
        default:
            setMapLayer(layers.defaultLayer)
    }
}
function mapZoom(zoomBtnId) {
    switch (zoomBtnId) {
        case "zoomInBtn":
            map.zoomIn(1,true)
            break
        case "zoomOutBtn":
            map.zoomOut(1,true)
            break
        default:
            map.setZoom(10)
            break
    }
}
function locateMapUser(lat, lng) {
    if (lat !== null && lng !== null) {
        userLocation.latitude = lat
        userLocation.longitude = lng
        L.marker([lat, lng], {icon: positionIcon}).addTo(map)
        map.setView([lat, lng], 13);
    }
}

function displayCircle(radius) {
    if (userLocation.longitude !== null && userLocation.latitude !== null) {
        circle.setLatLng([userLocation.latitude, userLocation.longitude])
        circle.setRadius(radius)
        circle.addTo(map)
    }
}

function getMapView() {
    let center = map.getCenter()
    return {
        lat : center.lat,
        lng: center.lng
    }
}

function getSearchResults(address) {
    let autocomplete = new google.maps.places.Autocomplete(address);
    let geocoder = new google.maps.Geocoder();
    console.log(autocomplete)
}
function performAutocomplete(address) {
    $(document).ready(function () {
        let autocomplete;
        autocomplete = new google.maps.places.AutocompleteService((address), {
            types: ['geocode'],
        });

        google.maps.event.addListener(autocomplete, 'place_changed', function () {
            let near_place = autocomplete.getPlace();
            console.log(near_place);
        });
    });
}

// functions calls
initiateMapSetup()
performAutocomplete('fes')
