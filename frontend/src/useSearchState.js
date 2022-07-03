import { useNavigate, useLocation } from "react-router-dom";
import { useCallback } from "react";

export default function useSearchState(name, defaultValue) {
  const { search } = useLocation();
  const params = new URLSearchParams(search);
  const navigate = useNavigate();
  const value = params.get(name);

  const setter = useCallback(
    (value) => {
      const params = new URLSearchParams(window.location.search);
      if (typeof value === "function")
        value = value(JSON.parse(params.get(name)));
      params.set(name, JSON.stringify(value));
      navigate("?" + params.toString());
    },
    [name, navigate]
  );

  let parsedValue;

  if (value !== null) {
    try {
      parsedValue = JSON.parse(value);
    } catch (e) {
      // Parsing failed. Revert to defaultValue.
    }
  }

  return [parsedValue === undefined ? defaultValue : parsedValue, setter];
}
