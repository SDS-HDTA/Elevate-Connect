import { defineStore } from 'pinia';
import { ref } from 'vue';
import request from '@/utils/request';

export const usePermissionStore = defineStore('permissions', () => {
  const hasAdminAccess = ref(false);
  const accessPermissions = ref([]);
  const createPermissions = ref([]);
  const deletePermissions = ref([]);
  const editPermissions = ref([]);
  const modifyPermissions = ref([]);
  const uploadPermissions = ref([]);

  const getPermissions = async () => {
    try {
      const res = await request.get(`/permissions`);

      if (res && res?.length) {
        res.forEach((permission) => {
          const [action, resource] = permission.split(':');

          // Admin should only have one permission in response
          if (
            action.toLowerCase() === 'admin' &&
            resource.toLowerCase() === 'all_permissions'
          ) {
            hasAdminAccess.value = true;
          }

          if (action.toLowerCase() === 'access') {
            accessPermissions.value.push(resource);
          }

          if (action.toLowerCase() === 'create') {
            createPermissions.value.push(resource);
          }

          if (action.toLowerCase() === 'delete') {
            deletePermissions.value.push(resource);
          }

          if (action.toLowerCase() === 'edit') {
            editPermissions.value.push(resource);
          }

          if (action.toLowerCase() === 'modify') {
            modifyPermissions.value.push(resource);
          }

          if (action.toLowerCase() === 'upload') {
            uploadPermissions.value.push(resource);
          }
        });
      }
    } catch (error) {
      console.error('Failed to fetch permissions:', error);
      hasAdminAccess.value = false;
      accessPermissions.value = [];
      createPermissions.value = [];
      deletePermissions.value = [];
      editPermissions.value = [];
      modifyPermissions.value = [];
      uploadPermissions.value = [];
    }
  };

  const hasPermission = (permission) => {
    if (hasAdminAccess.value) return true;
    if (!permission) return true;

    const [action, resource] = permission.split(':');
    const resourceLower = resource?.toLowerCase();

    switch (action.toLowerCase()) {
      case 'access':
        return accessPermissions.value.includes(resourceLower);
      case 'create':
        return createPermissions.value.includes(resourceLower);
      case 'delete':
        return deletePermissions.value.includes(resourceLower);
      case 'edit':
        return editPermissions.value.includes(resourceLower);
      case 'modify':
        return modifyPermissions.value.includes(resourceLower);
      case 'upload':
        return uploadPermissions.value.includes(resourceLower);
      default:
        return false;
    }
  };

  return {
    getPermissions,
    hasAdminAccess,
    hasPermission,
    accessPermissions,
    createPermissions,
    deletePermissions,
    editPermissions,
    modifyPermissions,
    uploadPermissions,
  };
});
