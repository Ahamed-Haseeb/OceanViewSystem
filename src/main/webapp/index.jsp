<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // SESSION PROTECTION: Redirect to login if not authenticated
    if(session.getAttribute("loggedUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort | Dashboard</title>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        :root { --primary: #0061f2; --dark: #212832; }
        body { font-family: 'Inter', sans-serif; background-color: #f2f5f9; color: var(--dark); }
        .navbar { background: linear-gradient(135deg, var(--primary) 0%, #6900cf 100%); padding: 1rem; }
        .card { border: none; border-radius: 1rem; box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.05); margin-bottom: 20px;}
        .btn-primary { background-color: var(--primary); border: none; padding: 10px 25px; border-radius: 0.5rem; transition: 0.3s; }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 4px 10px rgba(0,97,242,0.3); }
        .form-control, .form-select { border-radius: 0.5rem; padding: 12px; border: 1px solid #d1d9e6; }

        /* Table Colors fixed */
        .table th { background-color: #f8f9fa !important; font-weight: 600 !important; color: #000000 !important; border-bottom: 2px solid #e0e5ec !important; }
        .table td { color: #000000 !important; }
        .table-hover tbody tr:hover { background-color: #f1f5f9 !important; transition: 0.2s; }

        /* Custom Styles for the Bill Receipt */
        .receipt-body { font-family: 'Courier New', Courier, monospace; font-size: 1.1rem; color: #000; }
        .receipt-line { border-top: 2px dashed #000; margin: 15px 0; }
    </style>
</head>
<body onload="fetchAllReservations()">

<% if("1".equals(request.getParameter("success"))) { %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        Swal.fire({ title: 'Success!', text: 'Reservation saved successfully.', icon: 'success', confirmButtonColor: '#0061f2' });
    });
</script>
<% } else if("1".equals(request.getParameter("error"))) { %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        Swal.fire({ title: 'Error!', text: 'Failed to save reservation.', icon: 'error', confirmButtonColor: '#d33' });
    });
</script>
<% } %>

<nav class="navbar navbar-dark shadow-sm">
    <div class="container d-flex justify-content-between">
        <span class="navbar-brand fw-bold">🌊 OCEAN VIEW RESORT</span>
        <div class="d-flex align-items-center">
            <span class="text-white me-3 small">
                Welcome, <b><%= session.getAttribute("loggedUser") %></b>
                <span class="badge bg-warning text-dark ms-1"><%= session.getAttribute("userRole") %></span>
            </span>
            <a href="logout" class="btn btn-sm btn-danger fw-bold shadow-sm">Logout</a>
        </div>
    </div>
</nav>

<div class="container my-5">

    <% if("ADMIN".equals(session.getAttribute("userRole"))) { %>
    <div class="alert alert-info border-info shadow-sm d-flex justify-content-between align-items-center mb-4">
        <div>
            <h6 class="fw-bold mb-0">🛡️ Admin Control Panel</h6>
            <small>You have full access to system settings and user management.</small>
        </div>
        <button class="btn btn-dark btn-sm fw-bold" onclick="Swal.fire('Coming Soon', 'System Settings and User Management will be available in V2.0!', 'info')">Manage System</button>
    </div>
    <% } %>

    <div class="row g-4">
        <div class="col-lg-5 col-md-12">
            <div class="card p-4">
                <h5 class="fw-bold mb-3 text-primary">Add New Reservation</h5>
                <form action="reserve" method="post">
                    <div class="mb-2">
                        <label class="form-label small fw-bold">Guest Name</label>
                        <input type="text" name="guestName" class="form-control" placeholder="e.g. John Doe" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label small fw-bold">Contact Number</label>
                        <input type="text" name="contactNumber" class="form-control" placeholder="07XXXXXXXX" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label small fw-bold">Room Type</label>
                        <select name="roomId" class="form-select">
                            <option value="1">Single - Rs. 5000</option>
                            <option value="2">Double - Rs. 8000</option>
                            <option value="3">Suite - Rs. 15000</option>
                        </select>
                    </div>
                    <div class="mb-2">
                        <label class="form-label small fw-bold">Address</label>
                        <input type="text" name="address" class="form-control" placeholder="City / Town" required>
                    </div>
                    <div class="row">
                        <div class="col-6 mb-3">
                            <label class="form-label small fw-bold">Check-in</label>
                            <input type="date" name="checkInDate" class="form-control" required>
                        </div>
                        <div class="col-6 mb-3">
                            <label class="form-label small fw-bold">Check-out</label>
                            <input type="date" name="checkOutDate" class="form-control" required>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100 fw-bold shadow-sm">Save Reservation</button>
                </form>
            </div>

            <div class="card p-4 bg-light">
                <h6 class="fw-bold text-primary mb-2">ℹ️ Staff Help Guide</h6>
                <ul class="small text-muted ps-3 mb-0">
                    <li class="mb-1"><b>Book:</b> Fill the form & click Save.</li>
                    <li class="mb-1"><b>View:</b> Click 'View' in the table to see full details.</li>
                    <li class="mb-1"><b>Bill:</b> Enter 'Res ID' in the Bill section to print receipt.</li>
                </ul>
            </div>
        </div>

        <div class="col-lg-7 col-md-12">
            <div class="card p-4 mb-4 border-primary shadow-sm" style="border-width: 2px;">
                <h5 class="fw-bold mb-3 text-primary">Calculate & Print Bill</h5>
                <div class="input-group">
                    <input type="number" id="resNoInput" class="form-control form-control-lg" placeholder="Enter Reservation ID (e.g. 1)">
                    <button class="btn btn-primary px-4 fw-bold" onclick="fetchBill()">Generate Bill</button>
                </div>
            </div>

            <div class="card p-4 shadow-sm bg-white">
                <h5 class="fw-bold mb-3 text-primary">Recent Reservations</h5>
                <div class="table-responsive">
                    <table class="table table-hover align-middle bg-white">
                        <thead>
                        <tr>
                            <th class="text-dark">ID</th>
                            <th class="text-dark">Guest Name</th>
                            <th class="text-dark">Room</th>
                            <th class="text-dark">Check-in</th>
                            <th class="text-dark">Status</th>
                            <th class="text-center text-dark">Action</th>
                        </tr>
                        </thead>
                        <tbody id="reservationsTableBody">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
</div>

<div class="modal fade" id="billModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-dark text-white d-flex justify-content-center">
                <h5 class="modal-title fw-bold">OCEAN VIEW RESORT - BILL</h5>
            </div>
            <div class="modal-body p-4 bg-white receipt-body" id="printSection">
                <div class="text-center mb-4">
                    <strong>====================================</strong><br>
                    <strong>      OCEAN VIEW RESORT - BILL      </strong><br>
                    <strong>====================================</strong>
                </div>
                <p class="mb-2">Reservation No : <strong id="modalResNo"></strong></p>
                <p class="mb-2">Guest Name     : <strong id="modalGuestName"></strong></p>
                <p class="mb-2">Check-in Date  : <strong id="modalCheckIn"></strong></p>
                <p class="mb-2">Check-out Date : <strong id="modalCheckOut"></strong></p>
                <div class="receipt-line"></div>
                <h4 class="fw-bold text-end mt-3 mb-0">TOTAL : Rs. <span id="modalTotalBill"></span></h4>
                <div class="receipt-line"></div>
                <div class="text-center mt-3 small">Thank you for staying with us!</div>
            </div>
            <div class="modal-footer justify-content-center bg-light">
                <button type="button" class="btn btn-secondary px-4 fw-bold" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary px-4 fw-bold" onclick="printReceipt()">Print Bill</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="viewModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header text-white" style="background: linear-gradient(135deg, #0061f2 0%, #6900cf 100%);">
                <h5 class="modal-title fw-bold">Reservation Details (#<span id="v-resNo"></span>)</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <table class="table table-bordered mb-0">
                    <tbody>
                    <tr><th class="bg-light" width="35%">Guest Name</th><td id="v-name" class="fw-bold"></td></tr>
                    <tr><th class="bg-light">Contact No</th><td id="v-contact"></td></tr>
                    <tr><th class="bg-light">Address</th><td id="v-address"></td></tr>
                    <tr><th class="bg-light">Room Type</th><td id="v-room"></td></tr>
                    <tr><th class="bg-light">Check-In</th><td id="v-checkin"></td></tr>
                    <tr><th class="bg-light">Check-Out</th><td id="v-checkout"></td></tr>
                    <tr><th class="bg-light">Status</th><td><span class="badge" id="v-status"></span></td></tr>
                    <tr><th class="bg-light">Total Bill</th><td id="v-bill" class="fw-bold text-primary"></td></tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer bg-light">
                <button type="button" class="btn btn-secondary fw-bold" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    let reservationsList = [];

    async function fetchAllReservations() {
        try {
            const response = await fetch('all-reservations');
            const data = await response.json();
            reservationsList = data;

            const tableBody = document.getElementById('reservationsTableBody');
            tableBody.innerHTML = '';

            data.forEach(res => {
                let roomName = res.roomId === 1 ? 'Single' : (res.roomId === 2 ? 'Double' : 'Suite');

                let statusText = res.status ? res.status.toUpperCase() : 'PENDING';
                let badgeColor = statusText === 'PAID' ? 'bg-success text-white' : 'bg-warning text-dark';

                let actionBtn = '<button class="btn btn-sm btn-outline-primary fw-bold px-3 shadow-sm" onclick="viewReservation(' + res.reservationNo + ')">View</button>';

                let row = '<tr>' +
                    '<td><span class="badge bg-secondary">#' + res.reservationNo + '</span></td>' +
                    '<td class="fw-bold text-dark">' + res.guestName + '</td>' +
                    '<td class="text-dark">' + roomName + '</td>' +
                    '<td class="text-dark">' + res.checkInDate + '</td>' +
                    '<td><span class="badge ' + badgeColor + '">' + statusText + '</span></td>' +
                    '<td class="text-center">' + actionBtn + '</td>' +
                    '</tr>';

                tableBody.innerHTML += row;
            });
        } catch (e) {
            console.error('Error fetching reservations:', e);
        }
    }

    function viewReservation(id) {
        const res = reservationsList.find(r => r.reservationNo === id);
        if(res) {
            let roomName = res.roomId === 1 ? 'Single (Rs. 5000)' : (res.roomId === 2 ? 'Double (Rs. 8000)' : 'Suite (Rs. 15000)');

            let statusText = res.status ? res.status.toUpperCase() : 'PENDING';
            let badgeColor = statusText === 'PAID' ? 'bg-success text-white' : 'bg-warning text-dark';

            document.getElementById('v-resNo').innerText = res.reservationNo;
            document.getElementById('v-name').innerText = res.guestName;
            document.getElementById('v-contact').innerText = res.contactNumber;
            document.getElementById('v-address').innerText = res.address;
            document.getElementById('v-room').innerText = roomName;
            document.getElementById('v-checkin').innerText = res.checkInDate;
            document.getElementById('v-checkout').innerText = res.checkOutDate;

            let statusBadge = document.getElementById('v-status');
            statusBadge.innerText = statusText;
            statusBadge.className = 'badge ' + badgeColor;

            let billAmount = res.totalBill > 0 ? 'Rs. ' + res.totalBill.toLocaleString('en-US', {minimumFractionDigits: 1}) : 'Not Calculated Yet';
            document.getElementById('v-bill').innerText = billAmount;

            var viewPopup = new bootstrap.Modal(document.getElementById('viewModal'));
            viewPopup.show();
        }
    }

    async function fetchBill() {
        const resNo = document.getElementById('resNoInput').value;
        if(!resNo) {
            return Swal.fire({ title: 'Warning!', text: 'Please enter a Reservation ID.', icon: 'warning', confirmButtonColor: '#0061f2' });
        }

        try {
            const response = await fetch('bill?resNo=' + resNo);
            const data = await response.json();

            if(data && data.guestName) {
                document.getElementById('modalResNo').innerText = data.reservationNo;
                document.getElementById('modalGuestName').innerText = data.guestName;
                document.getElementById('modalCheckIn').innerText = data.checkInDate;
                document.getElementById('modalCheckOut').innerText = data.checkOutDate;
                document.getElementById('modalTotalBill').innerText = data.totalBill.toLocaleString('en-US', {minimumFractionDigits: 1, maximumFractionDigits: 1});

                var billPopup = new bootstrap.Modal(document.getElementById('billModal'));
                billPopup.show();

                document.getElementById('resNoInput').value = '';
                fetchAllReservations();
            } else {
                Swal.fire({ title: 'Not Found', text: 'Reservation not found in the database!', icon: 'error', confirmButtonColor: '#d33' });
            }
        } catch (e) {
            Swal.fire({ title: 'Error', text: 'Error generating bill. Make sure the ID is correct.', icon: 'error', confirmButtonColor: '#d33' });
        }
    }

    function printReceipt() {
        var printContents = document.getElementById('printSection').innerHTML;
        var originalContents = document.body.innerHTML;
        document.body.innerHTML = printContents;
        window.print();
        document.body.innerHTML = originalContents;
        window.location.reload();
    }
</script>

</body>
</html>