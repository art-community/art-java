export const buildRequest = (body) => ({
    method: 'POST',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    },
    data: JSON.stringify(body)
});