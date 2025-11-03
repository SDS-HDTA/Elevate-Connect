<template>
  <div
    :class="
      !permissionStore.hasPermission(permissions.AccessDiscover)
        ? ' cip-ccp-wrapper'
        : ' wrapper'
    "
  >
    <input
      ref="searchInput"
      class="search-box"
      type="text"
      placeholder="Search place…"
    />
    <div ref="mapRef" class="map"></div>
  </div>

  <AddEditMapMarker
    v-model="markerDialogVisible"
    :markerData="selectedMarker"
    :isEdit="isEditingMarker"
    @confirm="handleMarkerDialogConfirm"
  />
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { Loader } from '@googlemaps/js-api-loader';
import { ElMessageBox, ElMessage } from 'element-plus';
import request from '@/utils/request';
import { useRoute } from 'vue-router';
import { usePermissionStore } from '@/stores/permissionStore';
import { permissions } from '@/models/permission';
import { getMarkerImage, getMarkerTypeText } from '@/utils/markerTypeHelper';
import AddEditMapMarker from '@/views/dialogs/AddEditMapMarker.vue';

/* --------- Constants ---------- */
const API_KEY = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;
if (!API_KEY) {
  console.error('VITE_GOOGLE_MAPS_API_KEY environment variable is not set');
  ElMessage.error('Error connecting to Google Maps.');
}
const LIBS = ['places'];
const DEFAULT_CENTER = { lat: -33.86, lng: 151.2 }; // will always start in sydney
const DEFAULT_ZOOM = 10;

/* --------- DOM References ---------- */
const mapRef = ref(null);
const searchInput = ref(null);
const route = useRoute();
const projectId = route.params.id;
const markerDialogVisible = ref(false);
const isEditingMarker = ref(false);
const selectedMarker = ref(null);
let pendingLatLng = null;

/* --------- Runtime State ---------- */
let map, infoWindow;
const markers = []; // [{ id, marker, title, desc }]

const permissionStore = usePermissionStore();

/* --------- Lifecycle ---------- */
onMounted(async () => {
  await new Loader({ apiKey: API_KEY, libraries: LIBS, language: 'en' }).load();

  initMap();
  await fetchMarkersFromBackend(); // Get markers on initialization
});

onBeforeUnmount(() => {
  if (!map) return;
  const c = map.getCenter();
  localStorage.setItem(
    'map-center',
    JSON.stringify({ lat: c.lat(), lng: c.lng() })
  );
  localStorage.setItem('map-zoom', map.getZoom());
});

/* --------- Initialize Map & Search Box ---------- */
function initMap() {
  const savedCenter =
    JSON.parse(localStorage.getItem('map-center')) || DEFAULT_CENTER;
  const savedZoom = Number(localStorage.getItem('map-zoom')) || DEFAULT_ZOOM;

  map = new google.maps.Map(mapRef.value, {
    center: savedCenter,
    zoom: savedZoom,
  });
  infoWindow = new google.maps.InfoWindow();

  /* Click on blank area to add new marker */
  if (permissionStore.hasPermission(permissions.CreateMapMarker)) {
    map.addListener('click', (e) => createMarker(e.latLng));
  }

  /* Search box autocomplete */
  const sb = new google.maps.places.SearchBox(searchInput.value);
  sb.addListener('places_changed', async () => {
    const { Place } = await google.maps.importLibrary('places');

    const { places } = await Place.searchByText({
      textQuery: searchInput.value.value,
      fields: ['location'],
    });
    if (!places.length) return;
    map.panTo(places[0].location);
    map.setZoom(13);
  });
}

/* --------- Fetch markers from backend ---------- */
async function fetchMarkersFromBackend() {
  try {
    const response = await request.get(`/projects/${projectId}/markers`);
    const markersData = response.data || [];

    // Clear existing markers
    markers.forEach((m) => m.marker.setMap(null));
    markers.length = 0;

    // Create new markers
    markersData.forEach((data) => {
      const marker = new google.maps.Marker({
        position: { lat: data.lat, lng: data.lng },
        map,
        draggable: true,
        icon: {
          url: getMarkerImage(data.type),
          scaledSize: new google.maps.Size(32, 46),
        },
      });

      const markerData = {
        id: data.id,
        marker,
        title: data.title,
        desc: data.description,
        type: data.type,
      };
      markers.push(markerData);

      marker.addListener('dragstart', () => {
        marker.__originalPosition = marker.getPosition(); // save original
      });

      marker.addListener('dragend', (event) => {
        if (permissionStore.hasPermission(permissions.EditMapMarker)) {
          confirmAndUpdateDrag(markerData, event, marker);
        }
      });

      marker.addListener('click', () => openInfoWindow(markerData));
    });

    if (markers.length) {
      map.setCenter(markers[0].marker.getPosition()); // re-center map if there's markers
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: 'Failed to fetch markers',
      duration: 2000,
    });
    console.error('Get marker failed:', error);
  }
}

/* --------- Create marker ---------- */
function createMarker(latLng) {
  pendingLatLng = latLng;
  isEditingMarker.value = false;
  selectedMarker.value = { title: '', desc: '', type: 0 };
  markerDialogVisible.value = true;
}

/* --------- Edit marker ---------- */
function editMarker(data) {
  selectedMarker.value = { ...data };
  isEditingMarker.value = true;
  markerDialogVisible.value = true;
}

/* --------- Open editable InfoWindow ---------- */
function openInfoWindow(data) {
  const { id, marker, title, desc, type } = data;
  const lat = marker.getPosition().lat().toFixed(6);
  const lng = marker.getPosition().lng().toFixed(6);

  const el = document.createElement('div');
  el.className = 'marker-info-window';
  el.innerHTML = `
      <div class="marker-info-content">
        <h3 class="marker-title">${title}</h3>
        <p class="marker-label">Type:</p>
        <p class="marker-desc">${getMarkerTypeText(type)}</p>
        <p class="marker-label">Description:</p>
        <p class="marker-desc">${desc}</p>
        <p class="marker-label">Location:</p>
        <span class="marker-location"> ${lat}, ${lng}</span>
        <div class="marker-actions">
          <el-button class="btn-secondary" size="small" id="edit-${id}">
            <el-icon><Edit /></el-icon>
            Edit
          </el-button>
          <el-button class="btn-danger" size="small" id="del-${id}">
            <el-icon><Delete /></el-icon>
            Delete
          </el-button>
        </div>
      </div>
    `;

  infoWindow.setContent(el);
  infoWindow.open(map, marker);

  google.maps.event.addListenerOnce(infoWindow, 'domready', () => {
    document.getElementById(`edit-${id}`).onclick = () => {
      if (permissionStore.hasPermission(permissions.EditMapMarker)) {
        editMarker(data);
      }
    };
    document.getElementById(`del-${id}`).onclick = () => {
      if (permissionStore.hasPermission(permissions.DeleteMapMarker)) {
        deleteMarker(data);
      }
    };
  });
}

/* --------- Handle marker edit or add ---------- */
async function handleMarkerDialogConfirm({ title, description, type }) {
  if (isEditingMarker.value) {
    // --- Editing existing marker ---
    try {
      await request.put(`/projects/${projectId}/markers`, {
        id: selectedMarker.value.id,
        title,
        description,
        type,
      });
      selectedMarker.value.title = title;
      selectedMarker.value.desc = description;
      selectedMarker.value.type = type;

      deleteMarkerFunction(selectedMarker.value);
      setNewMarker(
        selectedMarker.value,
        selectedMarker.value.marker.getPosition()
      );
      openInfoWindow(selectedMarker.value);

      ElMessage.success('Marker updated successfully');
    } catch (error) {
      console.error(error);
      ElMessage.error('Update failed');
    }
  } else {
    // --- Creating new marker ---
    try {
      const response = await request.post(`/projects/${projectId}/markers`, {
        projectId: Number(projectId),
        lat: pendingLatLng.lat(),
        lng: pendingLatLng.lng(),
        title,
        description,
        type: type,
      });

      setNewMarker(
        {
          id: response.data.id,
          title,
          description,
          type,
        },
        pendingLatLng
      );

      ElMessage.success('Marker created successfully');
    } catch (error) {
      console.error(error);
      ElMessage.error('Failed to create marker');
    }
  }
}

/* --------- Delete marker ---------- */
function deleteMarker(data) {
  ElMessageBox.confirm(
    'Are you sure you want to delete this marker? This action cannot be undone.',
    'Confirm',
    {
      confirmButtonClass: 'btn-danger',
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
    }
  )
    .then(async () => {
      try {
        await request.delete(`/projects/${projectId}/markers`, {
          params: { id: data.id },
        });
        deleteMarkerFunction(data);
        infoWindow.close();
        ElMessage({
          type: 'success',
          message: 'Deleted successfully',
          duration: 2000,
        });
      } catch (error) {
        ElMessage({
          type: 'error',
          message: 'Failed to delete marker',
          duration: 2000,
        });
        console.error('Delete marker failed:', error);
      }
    })
    .catch(() => {});
}

const deleteMarkerFunction = (data) => {
  data.marker.setMap(null);
  const idx = markers.findIndex((m) => m.id === data.id);
  markers.splice(idx, 1);
};

const setNewMarker = (data, latLng) => {
  const marker = new google.maps.Marker({
    position: { lat: latLng.lat(), lng: latLng.lng() },
    map,
    draggable: true,
    icon: {
      url: getMarkerImage(data.type),
      scaledSize: new google.maps.Size(32, 46),
    },
  });

  const markerData = {
    id: data.id,
    marker,
    title: data.title,
    desc: data.description,
    type: data.type,
  };
  markers.push(markerData);

  marker.addListener('click', () => openInfoWindow(markerData));

  marker.addListener('dragstart', () => {
    marker.__originalPosition = marker.getPosition(); // save original
  });
  marker.addListener('dragend', (event) => {
    if (permissionStore.hasPermission(permissions.EditMapMarker)) {
      confirmAndUpdateDrag(markerData, event, marker);
    }
  });
};

const confirmAndUpdateDrag = (markerData, event, marker) => {
  const newLatLng = event.latLng;

  ElMessageBox.confirm(
    'Do you want to move this marker to the new location?',
    'Confirm Move',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }
  )
    .then(async () => {
      try {
        await request.put(`/projects/${projectId}/markers`, {
          id: markerData.id,
          lat: newLatLng.lat(),
          lng: newLatLng.lng(),
        });

        ElMessage({
          type: 'success',
          message: 'Marker location updated!',
          duration: 2000,
        });
      } catch (err) {
        ElMessage({
          type: 'error',
          message: 'Failed to update marker location',
          duration: 2000,
        });
        // Revert on API failure
        marker.setPosition(marker.__originalPosition);
      }
    })
    .catch(() => {
      // User cancelled, revert position
      marker.setPosition(marker.__originalPosition);
    });
};
</script>

<style scoped>
.wrapper {
  position: relative;
  height: calc(100vh - 232px);
  width: 100%;
}
.cip-ccp-wrapper {
  position: relative;
  height: calc(100vh - 232px);
  width: 100%;
}
.map {
  height: 100%;
  width: 100%;
}
.search-box {
  position: absolute;
  z-index: 10;
  top: 12px;
  left: 50%;
  transform: translateX(-50%);
  width: 320px;
  height: 40px;
  padding: 0 14px;
  border: 1px solid #bbb;
  border-radius: 6px;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
}

:deep(.marker-info-window) {
  min-width: 200px;
  font-family: 'Montserrat', sans-serif;
}

:deep(.marker-info-content) {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

:deep(.marker-title) {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

:deep(.marker-desc) {
  margin: 0;
  font-size: 12px;
  color: #606266;
}

:deep(.marker-label) {
  font-weight: 600;
  font-size: 12px;
  color: #303133;
}

:deep(.marker-location) {
  display: flex;
  align-items: start;
  font-size: 12px;
  color: #909399;
}

:deep(.marker-actions) {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 8px;
}

:deep(.btn-secondary) {
  cursor: pointer;
  background-color: transparent !important;
  border: 1px solid var(--color-primary) !important;
  color: var(--color-primary) !important;
  border-radius: 9999px !important;
  font-size: 14px;
  padding: 6px 12px !important;

  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

:deep(.btn-secondary:hover),
:deep(.btn-secondary:focus) {
  background-color: var(--color-primary-hover) !important;
  border-color: var(--color-primary-primary) !important;
  transform: translateY(-1px);
}

:deep(.btn-danger) {
  cursor: pointer;
  background-color: var(--color-danger) !important;
  border-color: var(--color-danger) !important;
  color: var(--color-white) !important;
  border-radius: 9999px !important;
  font-size: 14px;
  padding: 6px 12px !important;

  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

:deep(.btn-danger:hover),
:deep(.btn-danger:focus) {
  background-color: var(--color-danger-hover) !important;
  border-color: var(--color-danger-hover) !important;
  transform: translateY(-1px);
}

:deep(.gm-ui-hover-effect) {
  width: 40px !important;
  height: 28px !important;

  span {
    width: 18px !important;
    height: 18px !important;
  }
}
</style>
