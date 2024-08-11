<template>
  <component
    :is="props.tag"
    :type="props.tag === 'button' ? props.nativeType : ''"
    :disabled="props.disabled || props.loading"
    @click="handleClick"
    class="btn"
    :class="[
      { 'btn-round': props.round },
      { 'btn-block': props.block },
      { 'btn-icon btn-fab': props.icon },
      { [`btn-${props.type}`]: props.type },
      { [`btn-${props.size}`]: props.size },
      { 'btn-simple': props.simple },
      { 'btn-link': props.link },
      { disabled: props.disabled && props.tag !== 'button' },
    ]"
  >
    <slot name="loading">
      <i v-if="loading" class="fas fa-spinner fa-spin"></i>
    </slot>
    <slot></slot>
  </component>
</template>
<script setup>
import { ref, computed, useSlots, defineProps} from 'vue';


const props = defineProps({
  tag: {
    type: String,
  default: "button",
        description: "Button html tag",
  },
  round: Boolean,
      icon: Boolean,
      block: Boolean,
      loading: Boolean,
      disabled: Boolean,
      type: {
    type: String,
  default: "default",
        description: "Button type (primary|secondary|danger etc)",
  },
  nativeType: {
    type: String,
  default: "button",
        description: "Button native type (e.g button, input etc)",
  },
  size: {
    type: String,
  default: "",
        description: "Button size (sm|lg)",
  },
  simple: {
    type: Boolean,
        description: "Whether button is simple (outlined)",
  },
  link: {
    type: Boolean,
        description: "Whether button is a link (no borders or background)",
  },
},)


const slots = useSlots();
const emit = defineEmits(['click']);
const handleClick = (evt) => {
  try{
    emit('click', evt);
  }catch (e) {
    console.log(e.message);
  }

};



// export default {
//   name: "base-button",
//
//   methods: {
//     handleClick(evt) {
//       this.$emit("click", evt);
//     },
//   },
// };
</script>
<style></style>
