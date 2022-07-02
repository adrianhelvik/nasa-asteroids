import { Routes, BrowserRouter, Route } from "react-router-dom";
import React, { lazy } from "react";

const DetailsPage = lazy(() => import("./DetailsPage"));
const LandingPage = lazy(() => import("./LandingPage"));

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/asteroid/:id" element={<DetailsPage />} />
      </Routes>
    </BrowserRouter>
  );
}
