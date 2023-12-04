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

const map = L.map("map").setView([51.505, -0.09], 13);

const initialLayer = L.tileLayer('https://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
    maxZoom: 20,
    subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
}).addTo(map);

initiateMapSetup()

function initiateMapSetup(){
    map.zoomControl.remove();
}
function setMapLayer(mapLayer) {
    if(mapLayer.url !== "") {
        const newLayer = L.tileLayer(mapLayer.url, {subdomains: mapLayer.subdomains, maxZoom: mapLayer.maxZoom});
        map.eachLayer((layer) => {
            if (layer !== initialLayer) {
                map.removeLayer(layer);
            }
        });
        newLayer.addTo(map);
    }
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
