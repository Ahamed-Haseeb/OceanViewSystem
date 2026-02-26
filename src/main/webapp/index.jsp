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
        .table td { color: #000000 !important; vertical-align: middle; }
        .table-hover tbody tr:hover { background-color: #f1f5f9 !important; transition: 0.2s; }

        /* Custom Styles for the Bill Receipt */
        .receipt-body { font-family: 'Courier New', Courier, monospace; font-size: 1.1rem; color: #000; }
        .receipt-line { border-top: 2px dashed #000; margin: 15px 0; }

        /* Custom styling for the Help Modal List */
        .help-list li { margin-bottom: 10px; line-height: 1.6; }
    </style>
</head>
<body onload="fetchAllReservations()">

<% if("1".equals(request.getParameter("success"))) { %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        Swal.fire({ title: 'Success!', text: 'Reservation saved successfully.', icon: 'success', confirmButtonColor: '#0061f2' });
    });
</script>
<% } else if("1".equals(request.getParameter("updated"))) { %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        Swal.fire({ title: 'Updated!', text: 'Reservation updated successfully.', icon: 'success', confirmButtonColor: '#0061f2' });
    });
</script>
<% } else if("1".equals(request.getParameter("staffAdded"))) { %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        Swal.fire({ title: 'Staff Added!', text: 'New staff member registered securely.', icon: 'success', confirmButtonColor: '#0061f2' });
    });
</script>
<% } else if("date".equals(request.getParameter("error"))) { %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        Swal.fire({ title: 'Invalid Dates!', text: 'Check-out date must be AFTER the Check-in date.', icon: 'warning', confirmButtonColor: '#ffc107' });
    });
</script>
<% } else if("1".equals(request.getParameter("error"))) { %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        Swal.fire({ title: 'Error!', text: 'Action failed. Please try again.', icon: 'error', confirmButtonColor: '#d33' });
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

    <div class="alert alert-info border-info shadow-sm d-flex justify-content-between align-items-center mb-4">
        <div>
            <% if("ADMIN".equals(session.getAttribute("userRole"))) { %>
            <h6 class="fw-bold mb-0">🛡️ Admin Control Panel</h6>
            <small>You have full access to system settings and Staff management.</small>
            <% } else { %>
            <h6 class="fw-bold mb-0">👋 Staff Dashboard</h6>
            <small>Manage reservations and handle guest check-outs.</small>
            <% } %>
        </div>
        <div class="d-flex flex-column gap-2">
            <% if("ADMIN".equals(session.getAttribute("userRole"))) { %>
            <button class="btn btn-dark btn-sm fw-bold shadow-sm" onclick="openStaffModal()">👥 Manage Staff</button>
            <% } %>
            <button class="btn btn-secondary btn-sm fw-bold shadow-sm" onclick="openHelpModal()">ℹ️ Help Guide</button>
        </div>
    </div>

    <div class="row g-4">
        <div class="col-lg-4 col-md-12">
            <div class="card p-4 shadow-sm">
                <h5 class="fw-bold mb-3 text-primary">Add Reservation</h5>
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
                    <button type="submit" class="btn btn-primary w-100 fw-bold shadow-sm">Save Booking</button>
                </form>
            </div>
        </div>

        <div class="col-lg-8 col-md-12">

            <div class="card p-4 mb-4 border-primary shadow-sm" style="border-width: 2px;">
                <h5 class="fw-bold mb-3 text-primary">Calculate & Print Bill</h5>
                <div class="input-group">
                    <input type="number" id="resNoInput" class="form-control" placeholder="Enter Reservation ID (e.g. 1)">
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
                            <th class="text-dark">Status</th>
                            <th class="text-center text-dark">Actions</th>
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

<div class="modal fade" id="helpModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-secondary text-white">
                <h5 class="modal-title fw-bold">ℹ️ Ocean View Resort - System Help Guide</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4 text-dark">
                <h6 class="fw-bold text-primary mb-3">How to use the system:</h6>
                <ul class="help-list">
                    <li><b>🏨 Add a Reservation:</b> Fill out all the details in the "Add Reservation" form on the left. Make sure the Check-out date is AFTER the Check-in date, then click <b>Save Booking</b>.</li>
                    <li><b>👀 View Details:</b> Click the <span class="badge border border-primary text-primary">View</span> button in the table to see full information about a guest and their room.</li>
                    <li><b>✏️ Edit Booking:</b> Made a mistake? Click the <span class="badge border border-warning text-dark">Edit</span> button to change guest details, room types, or dates.</li>
                    <li><b>🗑️ Delete Booking:</b> Click the <span class="badge border border-danger text-danger">Delete</span> button to permanently remove a reservation. You will be asked to confirm first.</li>
                    <li><b>🧾 Generate Bill:</b> Check the 'ID' of the reservation from the table, type it into the <b>Calculate & Print Bill</b> box, and click Generate. A detailed printable receipt will appear!</li>
                    <% if("ADMIN".equals(session.getAttribute("userRole"))) { %>
                    <li><b>👥 Admin - Manage Staff:</b> As an Admin, you can click the "Manage Staff" button at the top to securely register new staff members or remove old accounts.</li>
                    <% } %>
                </ul>
                <div class="alert alert-light border mt-4 mb-0 text-center small">
                    Always remember to click the <b>Logout</b> button when leaving the counter to keep the system secure!
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="staffModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-dark text-white">
                <h5 class="modal-title fw-bold">👥 Manage Staff Members</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <div class="row">
                    <div class="col-md-5 border-end pe-4">
                        <h6 class="fw-bold mb-3 text-primary">Register New Staff</h6>
                        <form action="add-staff" method="post">
                            <div class="mb-2">
                                <label class="form-label small fw-bold">Username</label>
                                <input type="text" name="username" class="form-control" required placeholder="e.g. staff2">
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Password</label>
                                <input type="password" name="password" class="form-control" required placeholder="Will be hashed">
                            </div>
                            <button type="submit" class="btn btn-primary w-100 fw-bold shadow-sm">Create Staff</button>
                        </form>
                    </div>
                    <div class="col-md-7 ps-4">
                        <h6 class="fw-bold mb-3 text-primary">Active Staff Accounts</h6>
                        <div class="table-responsive" style="max-height: 250px; overflow-y: auto;">
                            <table class="table table-sm table-hover align-middle">
                                <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th class="text-center">Action</th>
                                </tr>
                                </thead>
                                <tbody id="staffTableBody">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-warning text-dark">
                <h5 class="modal-title fw-bold">Edit Reservation (#<span id="edit-resNo-display"></span>)</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form action="update-res" method="post">
                    <input type="hidden" name="reservationNo" id="edit-resNo">

                    <div class="mb-2">
                        <label class="form-label small fw-bold">Guest Name</label>
                        <input type="text" name="guestName" id="edit-name" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label small fw-bold">Contact Number</label>
                        <input type="text" name="contactNumber" id="edit-contact" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label small fw-bold">Room Type</label>
                        <select name="roomId" id="edit-room" class="form-select">
                            <option value="1">Single - Rs. 5000</option>
                            <option value="2">Double - Rs. 8000</option>
                            <option value="3">Suite - Rs. 15000</option>
                        </select>
                    </div>
                    <div class="mb-2">
                        <label class="form-label small fw-bold">Address</label>
                        <input type="text" name="address" id="edit-address" class="form-control" required>
                    </div>
                    <div class="row">
                        <div class="col-6 mb-3">
                            <label class="form-label small fw-bold">Check-in</label>
                            <input type="date" name="checkInDate" id="edit-checkin" class="form-control" required>
                        </div>
                        <div class="col-6 mb-3">
                            <label class="form-label small fw-bold">Check-out</label>
                            <input type="date" name="checkOutDate" id="edit-checkout" class="form-control" required>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-warning w-100 fw-bold shadow-sm">Update Reservation</button>
                </form>
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
                    <tr><th class="bg-light text-secondary">Handled By</th><td id="v-handler" class="text-secondary fw-bold"></td></tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer bg-light">
                <button type="button" class="btn btn-secondary fw-bold" data-bs-dismiss="modal">Close</button>
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

                <p class="mb-2">Handled By     : <strong id="modalHandler"></strong></p>

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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    let reservationsList = [];

    function openHelpModal() { new bootstrap.Modal(document.getElementById('helpModal')).show(); }

    async function openStaffModal() {
        await fetchStaffList();
        new bootstrap.Modal(document.getElementById('staffModal')).show();
    }

    async function fetchStaffList() {
        try {
            const res = await fetch('list-staff');
            const data = await res.json();
            const tbody = document.getElementById('staffTableBody');
            tbody.innerHTML = '';
            data.forEach(staff => {
                tbody.innerHTML += '<tr>' +
                    '<td>' + staff.id + '</td>' +
                    '<td class="fw-bold">' + staff.username + '</td>' +
                    '<td class="text-center">' +
                    '<button class="btn btn-sm btn-outline-danger py-0 px-2" onclick="deleteStaff(' + staff.id + ')">Remove</button>' +
                    '</td>' +
                    '</tr>';
            });
        } catch(e) { console.error('Error fetching staff', e); }
    }

    function deleteStaff(id) {
        Swal.fire({
            title: 'Remove Staff?',
            text: "Are you sure you want to delete this account?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            confirmButtonText: 'Yes, remove!'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch('delete-staff?id=' + id)
                    .then(r => r.json())
                    .then(d => {
                        if(d.success) {
                            Swal.fire('Removed!', 'Staff account deleted.', 'success');
                            fetchStaffList();
                        } else {
                            Swal.fire('Error!', 'Could not delete.', 'error');
                        }
                    });
            }
        });
    }

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

                let actionBtn = '<button class="btn btn-sm btn-outline-primary fw-bold px-2 m-1 shadow-sm" onclick="viewReservation(' + res.reservationNo + ')">View</button>' +
                    '<button class="btn btn-sm btn-outline-warning fw-bold px-2 m-1 shadow-sm text-dark" onclick="openEditModal(' + res.reservationNo + ')">Edit</button>' +
                    '<button class="btn btn-sm btn-outline-danger fw-bold px-2 m-1 shadow-sm" onclick="deleteReservation(' + res.reservationNo + ')">Delete</button>';

                let row = '<tr>' +
                    '<td><span class="badge bg-secondary">#' + res.reservationNo + '</span></td>' +
                    '<td class="fw-bold text-dark">' + res.guestName + '</td>' +
                    '<td class="text-dark">' + roomName + '</td>' +
                    '<td><span class="badge ' + badgeColor + '">' + statusText + '</span></td>' +
                    '<td class="text-center">' + actionBtn + '</td>' +
                    '</tr>';

                tableBody.innerHTML += row;
            });
        } catch (e) { console.error('Error fetching reservations:', e); }
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
            document.getElementById('v-handler').innerText = res.createdBy ? res.createdBy : 'Unknown';

            let statusBadge = document.getElementById('v-status');
            statusBadge.innerText = statusText;
            statusBadge.className = 'badge ' + badgeColor;

            let billAmount = res.totalBill > 0 ? 'Rs. ' + res.totalBill.toLocaleString('en-US', {minimumFractionDigits: 1}) : 'Not Calculated Yet';
            document.getElementById('v-bill').innerText = billAmount;

            new bootstrap.Modal(document.getElementById('viewModal')).show();
        }
    }

    function openEditModal(id) {
        const res = reservationsList.find(r => r.reservationNo === id);
        if(res) {
            document.getElementById('edit-resNo').value = res.reservationNo;
            document.getElementById('edit-resNo-display').innerText = res.reservationNo;
            document.getElementById('edit-name').value = res.guestName;
            document.getElementById('edit-contact').value = res.contactNumber;
            document.getElementById('edit-room').value = res.roomId;
            document.getElementById('edit-address').value = res.address;
            document.getElementById('edit-checkin').value = res.checkInDate;
            document.getElementById('edit-checkout').value = res.checkOutDate;

            new bootstrap.Modal(document.getElementById('editModal')).show();
        }
    }

    function deleteReservation(id) {
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this! (ID: " + id + ")",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch('delete-res?resNo=' + id)
                    .then(response => response.json())
                    .then(data => {
                        if(data.success) {
                            Swal.fire('Deleted!', 'Reservation has been deleted.', 'success');
                            fetchAllReservations();
                        } else {
                            Swal.fire('Error!', 'Failed to delete.', 'error');
                        }
                    });
            }
        });
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
                document.getElementById('modalHandler').innerText = data.createdBy ? data.createdBy : 'Unknown';
                document.getElementById('modalTotalBill').innerText = data.totalBill.toLocaleString('en-US', {minimumFractionDigits: 1, maximumFractionDigits: 1});

                new bootstrap.Modal(document.getElementById('billModal')).show();

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