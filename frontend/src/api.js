const apiUrl = import.meta.env.VITE_API_URL;

export default {
  async getAsteroidsInWeek(week, { potentiallyHazardous }) {
    const res = await fetch(
      `${apiUrl}/v1/weekly-asteroids/${week}?potentially-hazardous=${potentiallyHazardous}`
    );
    const data = await res.json();
    return data;
  },
  async getAsteroidById(id) {
    const res = await fetch(`${apiUrl}/v1/asteroids/${id}`);
    const data = await res.json();
    return data;
  },
};
