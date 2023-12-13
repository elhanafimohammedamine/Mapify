
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
    iconUrl: './assets/images/positionIcon.png',
    iconSize: [40, 40],
    iconAnchor: [22, 22],
    popupAnchor: [-3, -76],
    shadowUrl: './assets/images/IconShadow.png',
    shadowSize: [40, 40],
    shadowAnchor: [22, 22],
});
const normalPositionIcon = L.icon({
    iconUrl: './assets/images/normalPosition.png',
    iconSize: [40, 40],
    iconAnchor: [22, 22],
    popupAnchor: [-3, -76],
    shadowUrl: './assets/images/IconShadow.png',
    shadowSize: [40, 40],
    shadowAnchor: [22, 22],
});
const userIcon = L.icon({
    iconUrl: './assets/images/userIcon.png',
    iconSize: [40, 40],
    iconAnchor: [22, 22],
    popupAnchor: [-3, -76],
    shadowUrl: './assets/images/IconShadow.png',
    shadowSize: [40, 40],
    shadowAnchor: [22, 22],
});
let circle = L.circle([null, null], {
    color: '#4025d6',
    fillColor: '#5c3eff',
    fillOpacity: 0.4,
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
function goToLocation(lat, lng, icon) {
    if (lat !== null && lng !== null) {
        userLocation.latitude = lat
        userLocation.longitude = lng
        L.marker([lat, lng], {icon: icon}).addTo(map)
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
// functions calls
initiateMapSetup()

/*
async function performAutocomplete(address) {
    let resultingPlaces = []
    let promise = new Promise((resolve, reject) => {
        let geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': address}, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK && results && results.length > 0) {
                let location = results[0].geometry.location;
                let autocompleteService = new google.maps.places.AutocompleteService();
                autocompleteService.getPlacePredictions({
                    input: address,
                    types: ['geocode'],
                    location: location,
                    radius: 1000
                }, function (predictions, status) {
                    if (status === google.maps.places.PlacesServiceStatus.OK && predictions) {
                        let placeService = new google.maps.places.PlacesService(document.createElement('div'));
                        predictions.forEach(function (prediction) {
                            placeService.getDetails({placeId: prediction.place_id}, function (place, status) {
                                if (status === google.maps.places.PlacesServiceStatus.OK && place) {
                                    let placeObj = {
                                        placeAddress: place.formatted_address,
                                        placeLat: place.geometry.location.lat(),
                                        placeLng: place.geometry.location.lng(),
                                        placeId: place.place_id
                                    }
                                    resultingPlaces.push(placeObj)
                                }
                                resolve(resultingPlaces);
                            });
                        });
                    } else {
                        reject('No predictions found');
                    }
                });
            } else {
                reject('Geocoder failed');
            }
        })
    })
    resultingPlaces = await promise;
    return resultingPlaces
}

 */


