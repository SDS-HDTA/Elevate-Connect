<template>
  <div class="avatar" :style="avatarStyle">
    {{ initials }}
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  firstName: String,
  lastName: String,
  size: {
    type: Number,
    default: 40
  }
})

const initials = computed(() => {
  const f = props.firstName?.[0] || ''
  const l = props.lastName?.[0] || ''
  return (f + l).toUpperCase()
})

function stringToColor(str) {
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash)
  }
  let color = '#'
  for (let i = 0; i < 3; i++) {
    color += ('00' + ((hash >> (i * 8)) & 0xff).toString(16)).slice(-2)
  }
  return color
}
const bgColor = computed(() => stringToColor((props.firstName || '') + (props.lastName || '')))
const avatarStyle = computed(() => ({
  backgroundColor: bgColor.value,
  width: props.size + 'px',
  height: props.size + 'px',
  fontSize: (props.size * 0.5) + 'px'
}))
</script>

<style scoped>
.avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  color: #fff;
  font-weight: bold;
  user-select: none;
}
</style> 