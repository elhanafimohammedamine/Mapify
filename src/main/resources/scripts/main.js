
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
const userPositionIcon = L.icon({
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
let userLocation = null
let openedPopup = null;
let previousRouting = null;
let routePolyline = null;
let usersMarkers = L.layerGroup();



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
let deviceMarker = null
let locationMarker = null
function goToDeviceLocation(lat, lng) {
    if (lat !== null && lng !== null) {
        if (locationMarker) {
            map.removeLayer(locationMarker)
        }
        if (userLocation == null){
            userLocation = {
                lat: lat,
                lng: lng
            }
            if (!deviceMarker) {
                deviceMarker = L.marker([lat, lng], {icon: userPositionIcon}).addTo(map)
            }
        }
        map.setView([lat, lng], 12);
    }
}

function goToLocation(lat, lng) {
    if (lat !== null && lng !== null) {
        if (locationMarker){
            map.removeLayer(locationMarker)
            locationMarker = L.marker([lat, lng], {icon: normalPositionIcon}).addTo(map)
        }
        else {
            locationMarker = L.marker([lat, lng], {icon: normalPositionIcon}).addTo(map)
        }
        map.setView([lat, lng], 12);
    }
}
function setUsersMarker(locations, deviceLat, deviceLng) {
    usersMarkers.clearLayers();
    if (locations.length > 0) {
        let firstUserLat = locations[0].lat
        let firstUserLng = locations[0].lng
        locations.forEach(function(location) {
            let marker = L.marker([location.lat, location.lng], {icon: userIcon});
            marker.on('click', () => {
                routingTrack(deviceLat, deviceLng, location.lat, location.lng)
            })
            usersMarkers.addLayer(marker)
        });
        usersMarkers.addTo(map)
        map.setView([firstUserLat, firstUserLng], 12);
    }
}

function checkIfRouteInMapAndRemoveIt() {
    if (routePolyline) {
        map.removeLayer(routePolyline)
    }
}
function displayCircle(radius) {
    if (userLocation !== null) {
        checkIfRouteInMapAndRemoveIt()
        circle.setLatLng([userLocation.lat, userLocation.lng])
        circle.setRadius(radius)
        circle.addTo(map);
    }
}
function routingTrack(originLat, originLng, destinationLat, destinationLng) {
    let currentRouting = [originLat, originLng, destinationLat, destinationLng].join(',');
    if (previousRouting === currentRouting) {
        return;
    }
    checkIfRouteInMapAndRemoveIt()
    let control = L.Routing.control({
        waypoints: [
            L.latLng(originLat, originLng),
            L.latLng(destinationLat, destinationLng)
        ],
        show: false
    }).addTo(map)
    control.on('routesfound', function(e) {
        let route = e.routes[0];
        if (!deviceMarker) {
            deviceMarker = L.marker([originLat, originLng], {icon: userPositionIcon}).addTo(map)
        }
        routePolyline = L.polyline(route.coordinates, {
            color: '#553cff',
            opacity: 1,
            weight: 6
        }).addTo(map);
        map.fitBounds(routePolyline.getBounds());
        map.removeControl(control);
    })
    previousRouting = currentRouting;
}

//routingTrack(35.1646954,-3.8553517,35.1721635,-3.8628263)
function displayPopup(user) {
    let userObject = JSON.parse(user);
    let location = L.latLng(userObject.lat, userObject.lng);
    map.setView(location, 12);
    let popupContent = popUpHtmlContent(userObject.fullName, userObject.address, userObject.phoneNumber);
    let newPopup = L.popup().setLatLng(location).setContent(popupContent);
    if (openedPopup) {
        if (openedPopup !== newPopup) {
            map.removeLayer(openedPopup)
            newPopup.addTo(map)
            openedPopup = newPopup
        }
        else {
            openedPopup.addTo(map)
        }
    }
    else {
        newPopup.addTo(map)
        openedPopup = newPopup
    }
}
function getMapView() {
    let center = map.getCenter()
    return {
        lat : center.lat,
        lng: center.lng
    }
}

function checkIfPopupOpened() {
    if (openedPopup) {
        map.removeLayer(openedPopup)
    }
}
function popUpHtmlContent(name, address, phoneNumber) {
    return '<div style="display: flex; flex-direction: column; justify-content: center; align-items: start; row-gap: 8px;">' +
        '<div style="display: flex; justify-content: center; align-items: center; column-gap: 10px;">' +
        '<span style="margin: 0; padding: 0;">' +
        '<svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-person-fill" style="width: 18px; aspect-ratio: 1/1; color: #3e3fff;" viewBox="0 0 16 16">' +
        '<path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>' +
        '</svg>' +
        '</span>' +
        '<p style="font-family: Outfit, sans-serif; font-size: 14px; font-weight: 400; color: #2d2c36; margin: 0; padding: 0;">' + name + '</p>' +
        '</div>' +
        '<div style="display: flex; justify-content: center; align-items: center; column-gap: 10px;">' +
        '<span style="margin: 0; padding: 0;">' +
        '<svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-geo-alt-fill" style="width: 18px; aspect-ratio: 1/1; color: #3e3fff;" viewBox="0 0 16 16">' +
        '<path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10m0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6"/>' +
        '</svg>' +
        '</span>' +
        '<p style="font-family: Outfit, sans-serif; font-size: 14px; font-weight: 400; color: #2d2c36; margin: 0; padding: 0;">' + address + '</p>' +
        '</div>' +
        '<div style="display: flex; justify-content: center; align-items: center; column-gap: 10px;">' +
        '<span style="margin: 0; padding: 0;">' +
        '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" style="width: 18px; aspect-ratio: 1/1; color: #3e3fff;">' +
        '<path fill-rule="evenodd" d="M1.5 4.5a3 3 0 0 1 3-3h1.372c.86 0 1.61.586 1.819 1.42l1.105 4.423a1.875 1.875 0 0 1-.694 1.955l-1.293.97c-.135.101-.164.249-.126.352a11.285 11.285 0 0 0 6.697 6.697c.103.038.25.009.352-.126l.97-1.293a1.875 1.875 0 0 1 1.955-.694l4.423 1.105c.834.209 1.42.959 1.42 1.82V19.5a3 3 0 0 1-3 3h-2.25C8.552 22.5 1.5 15.448 1.5 6.75V4.5Z" clip-rule="evenodd" />' +
        '</svg>' +
        '</span>' +
        '<p style="font-family: Outfit, sans-serif; font-size: 14px; font-weight: 400; color: #2d2c36; margin: 0; padding: 0;">' + phoneNumber + '</p>' +
        '</div>' +
        '</div>';
}

// functions calls
initiateMapSetup()



