import HeartBrokenIcon from '@mui/icons-material/HeartBroken'
import React, { useState, useEffect, useMemo } from 'react'
import ErrorIcon from '@mui/icons-material/Error'
import styled from 'styled-components'
import { getISOWeek } from 'date-fns'
import Loading from 'react-loading'
import Module from './Module'
import api from '../api'

const WEEK_COLUMNS = 5
const WEEK_ROWS = 4

export default function LandingPage() {
  const [currentWeek] = useState(getISOWeek(new Date()))
  const [selectedWeek, setSelectedWeek] = useState(currentWeek)
  const [asteroids, setAsteroids] = useState()
  const [error, setError] = useState()

  const weeks = useMemo(() => {
    const weeks = []
    for (let i = 0; i < WEEK_COLUMNS * WEEK_ROWS; i++) {
      if (currentWeek - i >= 1) {
        weeks.unshift(currentWeek - i)
      } else {
        // I would have handled weeks around the year change
        // better here.
        break
      }
    }
    return weeks
  }, [currentWeek])

  useEffect(() => {
    if (error) return
    let cancelled = false
    setAsteroids(null)
    api.getAsteroidsInWeek(selectedWeek).then(
      ({ data }) => {
        if (cancelled) return
        setAsteroids(data)
      },
      (error) => {
        setError(error.message)
      },
    )
    return () => {
      cancelled = true
    }
  }, [selectedWeek, error])

  return (
    <Container>
      <Header>
        <span className="bx bx-meteor" />
        <h1>Asteroid Watcher</h1>
      </Header>
      <Main>
        <Module title="Week">
          <Grid>
            {weeks.map((week) => (
              <Week
                key={week}
                onClick={() => {
                  setSelectedWeek(week)
                }}
                $active={selectedWeek === week}
              >
                {week}
              </Week>
            ))}
          </Grid>
        </Module>
        <Module title="Largest asteroids">
          {asteroids == null && !error && <StyledLoading type="bubbles" />}
          {asteroids == null && error && (
            <ErrorMessage>
              <HeartBrokenIcon />
              <div>{error}</div>
              <Button onClick={() => setError(null)}>Retry</Button>
            </ErrorMessage>
          )}
          {asteroids?.map?.((asteroid) => (
            <Asteroid key={asteroid.id}>
              <div>
                <div>
                  <strong>
                    {asteroid.name}{' '}
                    {asteroid.is_potentially_hazardous && (
                      <ErrorIcon
                        title="Potentially hazardous"
                        style={{ color: 'var(--red)' }}
                      />
                    )}
                  </strong>
                </div>
                <div>
                  Nearest miss:{' '}
                  {new Intl.NumberFormat('en-US').format(
                    Math.round(asteroid.miss_distance_km),
                  )}{' '}
                  km
                </div>
              </div>
              <a href={`/asteroid/${asteroid.id}`}>See more</a>
            </Asteroid>
          ))}
        </Module>
      </Main>
    </Container>
  )
}

const Container = styled.main`
  height: 100%;
`

const Main = styled.main`
  padding: 20px;
  display: grid;
  grid-template-columns: 4fr 6fr;
  grid-auto-flow: row;
  gap: 10px;
  max-width: 900px;
  margin: 0 auto;
`

const Header = styled.header`
  height: 100px;
  color: var(--accent);
  text-align: center;
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;

  h1 {
    font-weight: normal;
  }

  & > * {
    font-size: 50px;
  }
`

const Week = styled.button`
  border: none;
  font-weight: normal;
  cursor: pointer;
  aspect-ratio: 1 / 1;
  background-color: ${(p) => (p.$active ? 'white' : '#aaa')};
  color: var(--dark);
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 25px;
`

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(${WEEK_COLUMNS}, 1fr);
  gap: 10px;
`

const StyledLoading = styled(Loading)`
  margin: 40px auto;
`

const Asteroid = styled.div`
  margin-bottom: 10px;
  background-color: white;
  padding: 5px;
  color: var(--dark);
  display: grid;
  grid-template-columns: 1fr auto;
  a {
    background-color: var(--dark-accent);
    color: white;
    padding: 10px;
    border-radius: 5px;
    text-decoration: none;
    cursor: pointer;
    transition: background-color 0.1s;
    :hover {
      background-color: var(--mid-accent);
    }
    :hover:active {
      background-color: var(--dark);
    }
  }
`

const ErrorMessage = styled.div`
  text-align: center;
  margin: 50px;
`

const Button = styled.button`
  border-radius: 5px;
  border: none;
  background-color: white;
  color: var(--dark);
  padding: 5px 20px;
  margin-top: 15px;
  cursor: pointer;

  :hover {
    background-color: #eee;
  }

  :hover:active {
    background-color: #aaa;
  }
`
