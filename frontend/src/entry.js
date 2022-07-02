import { createRoot } from "react-dom/client";
import { createElement } from "react";
import Routes from "./Routes";

const root = createRoot(document.getElementById("root"));

root.render(createElement(Routes));
