import styled from "styled-components";
import React from "react";

export default function fmtDiameter(diameter) {
  const large = diameter.meters.estimated_diameter_max >= 1000;

  if (large) {
    return (
      <Large>
        {min(diameter)} - {max(diameter)}
      </Large>
    );
  }

  return (
    <>
      {min(diameter)} - {max(diameter)}
    </>
  );
}

function min(diameter) {
  if (diameter.meters.estimated_diameter_min < 1000) {
    return <>{diameter.meters.estimated_diameter_min.toFixed(1)}m</>;
  }
  return <>{diameter.kilometers.estimated_diameter_min.toFixed(1)}km</>;
}

function max(diameter) {
  if (diameter.meters.estimated_diameter_max < 1000) {
    return <>{diameter.meters.estimated_diameter_max.toFixed(1)}m</>;
  }
  return <>{diameter.kilometers.estimated_diameter_max.toFixed(1)}km</>;
}

const Large = styled.strong`
  color: var(--red);
`;
