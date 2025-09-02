<template>
  <div class="avatar" :style="avatarStyle">
    {{ initials }}
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  username: {
    type: String,
    required: true,
  },
  size: {
    type: Number,
    default: 40,
  },
});

// Process username to get initials
const initials = computed(() => {
  const nameParts = props.username.split(' ');
  const firstInitial = nameParts[0]?.[0] || '';
  const lastInitial = nameParts[1]?.[0] || '';
  return (firstInitial + lastInitial).toUpperCase();
});

const avatarStyle = computed(() => ({
  width: props.size + 'px',
  height: props.size + 'px',
  fontSize: props.size * 0.5 + 'px',
}));
</script>

<style scoped>
.avatar {
  display: flex;
  background-color: #106a52;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  color: #fff;
  font-weight: bold;
  user-select: none;
}
</style>
