import { Ring } from "@uiball/loaders";

function Loading() {
  return (
    <div
      style={{
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        zIndex: 9999,
      }}
    >
      <Ring size={80} lineWeight={4} speed={2} color="white" />
    </div>
  );
}

export default Loading;
