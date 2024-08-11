<template>
  <div
      class="form-group"
      :class="{
      'input-group': hasIcon,
      'input-group-focus': focused,
    }"
  >
    <slot name="label">
      <label v-if="label" class="control-label">
        {{ label }}
      </label>
    </slot>
    <slot name="addonLeft">
      <span v-if="addonLeftIcon" class="input-group-prepend">
        <div class="input-group-text">
          <i :class="addonLeftIcon"></i>
        </div>
      </span>
    </slot>

    <slot>
      <input
          :value="modelValue"
          :type="type"
          v-bind="$attrs"
          @input="onInput"
          @blur="onBlur"
          @focus="onFocus"
          class="form-control"
          aria-describedby="addon-right addon-left"
      />
    </slot>

    <slot name="addonRight">
      <span v-if="addonRightIcon" class="input-group-append">
        <div class="input-group-text">
          <i :class="addonRightIcon"></i>
        </div>
      </span>
    </slot>
    <slot name="helperText"></slot>
  </div>
</template>

<script setup>
import { ref, computed, useSlots, defineProps, defineEmits } from 'vue';

const props = defineProps({
  label: {
    type: String,
    description: "Input label",
  },
  type: {
    type: String,
    default: "text",
    description: "Input type",
  },
  modelValue: {
    type: [String, Number],
    description: "Input value",
    default: null,
  },
  addonRightIcon: {
    type: String,
    description: "Input icon on the right",
  },
  addonLeftIcon: {
    type: String,
    description: "Input icon on the left",
  },
});

const focused = ref(false);
const slots = useSlots();

const hasIcon = computed(() => {
  return (
      props.addonRightIcon !== undefined ||
      props.addonLeftIcon !== undefined ||
      slots.addonRight !== undefined ||
      slots.addonLeft !== undefined
  );
});

const emit = defineEmits(['update:modelValue']);
const onInput = (evt) => {
  emit("update:modelValue", evt.target.value);
};

const onFocus = () => {
  focused.value = true;
};

const onBlur = () => {
  focused.value = false;
};
</script>