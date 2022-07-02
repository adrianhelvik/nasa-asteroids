const apiUrl = import.meta.env.VITE_API_URL;

console.log(apiUrl);

export default {
  async getAsteroidsInWeek(week) {
    return new Promise(() => {});
    /*
    const res = await fetch(+`${apiUrl}/v1/asteroids?from=${from}&to=${to}`);
    const data = await res.json();
    return data;
    */
  },
};
