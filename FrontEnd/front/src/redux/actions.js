export const setStartAction = (value) => {
  return {
    type: "SET_START_ACTION",
    payload: value,
  };
};

export const setAfterAction = (value) => {
  return {
    type: "SET_AFTER_ACTION",
    payload: value,
  };
};
