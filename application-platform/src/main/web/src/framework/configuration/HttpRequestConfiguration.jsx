export const buildJsonRequest = (body, method = 'POST') => ({
    method: method,
    credentials: false,
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    data: JSON.stringify(body)
});