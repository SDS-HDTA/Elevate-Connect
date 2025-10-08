import { usePermissionStore } from '@/stores/permissionStore';
import { watch } from 'vue';

export default {
  install(app) {
    const createPermissionDirective = () => ({
      mounted(el, binding) {
        const store = usePermissionStore();

        const checkPermission = () => {
          if (!binding.value || !binding.value.includes(':')) {
            console.warn(
              `Invalid permission binding: "${binding.value}". Expected format: "action:resource"`
            );
            return;
          }

          const [action, resource] = binding.value
            .split(':')
            .map((v) => v.toLowerCase());

          const hasAdmin = store.hasAdminAccess;
          const permissions = store[`${action}Permissions`]?.value || [];
          const allowed = hasAdmin || permissions.includes(resource);

          if (!allowed) {
            // hide - can still edit in dev tools, but BE has final say
            el.style.display = 'none';
          } else {
            el.style.display = ''; // re-show if allowed
          }
        };

        checkPermission();

        // Reactively recheck when store updates (after async API call)
        watch(
          () => [
            store.hasAdminAccess,
            store.accessPermissions,
            store.createPermissions,
            store.deletePermissions,
            store.editPermissions,
            store.modifyPermissions,
            store.uploadPermissions,
          ],
          checkPermission,
          { deep: true }
        );
      },
    });

    app.directive('require-permission', createPermissionDirective());
  },
};
