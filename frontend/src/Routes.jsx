import { Routes, BrowserRouter, Route } from "react-router-dom";
import React, { lazy } from "react";

const LandingPage = lazy(() => import("./LandingPage"));

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
      </Routes>
    </BrowserRouter>
  );
}
