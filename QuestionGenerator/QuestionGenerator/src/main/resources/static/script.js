async function generate() {
    const role = document.getElementById('role').value;
    const experience = document.getElementById('experience').value;

    const res = await fetch('http://localhost:8080/api/generate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `role=${role}&experience=${experience}`
    });

    const data = await res.text();
    document.getElementById('result').innerText = data;
}
