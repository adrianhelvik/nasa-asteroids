import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import styled from 'styled-components'
import Loading from 'react-loading'
import api from '../api'

export default function DetailsPage() {
  const [asteroid, setAsteroid] = useState(null)
  const { id } = useParams()

  useEffect(() => {
    let cancelled = false
    api.getAsteroidById(id).then((asteroid) => {
      if (cancelled) return
      setAsteroid(asteroid)
    })
    return () => {
      cancelled = true
    }
  }, [id])

  if (asteroid == null) return <StyledLoading type="bubbles" />

  console.log(asteroid)

  return (
    <div>
      <h1>{asteroid.name}</h1>
      <table>
        <thead />
        <tbody>
          <tr>
            <th>Nearest miss</th>
            <td>
              {new Intl.NumberFormat('en-US').format(
                Math.round(asteroid.miss_distance_km),
              )}{' '}
              km
            </td>
          </tr>
          <tr>
            <th>Estimated diameter</th>
            <td>
              {asteroid.estimated_diameter.meters.estimated_diameter_min.toFixed(
                2,
              )}
              m to{' '}
              {asteroid.estimated_diameter.meters.estimated_diameter_max.toFixed(
                2,
              )}
              m
            </td>
          </tr>
        </tbody>
      </table>
      {asteroid.is_potentially_hazardous && <Hazard>Potentially hazardous!</Hazard>}
    </div>
  )
}

const StyledLoading = styled(Loading)`
  margin: 40px auto;
`

const Hazard = styled.div`
  font-size: 30px;
  color: red;
`
