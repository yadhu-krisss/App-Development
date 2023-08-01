package com.example.application;

import java.util.List;
import java.util.Scanner;

import com.example.dao.AppointmentDAO;
import com.example.dao.BookingDAO;
import com.example.dao.CounselorDAO;
import com.example.dao.StudentDAO;
import com.example.entity.Appointment;
import com.example.entity.Booking;
import com.example.entity.Counselor;
import com.example.entity.Student;
import com.example.interfc.AppointmentInterface;
import com.example.interfc.BookingInterface;
import com.example.interfc.CounselorInterface;
import com.example.interfc.StudentInterface;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        StudentInterface studentDAO = new StudentDAO();
        CounselorInterface counselorDAO = new CounselorDAO();
        AppointmentInterface appointmentDAO = new AppointmentDAO();
        BookingInterface bookingDAO = new BookingDAO();

        int userType;
        do {
            System.out.println("-------------- Student Counseling Appointment System ------------");
            System.out.println("1. Student");
            System.out.println("2. Counselor");
            System.out.println("3. Exit");
            System.out.print("Enter your user type (1 for Student, 2 for Counselor, 3 to Exit): ");
            userType = scanner.nextInt();
            System.out.println();

            switch (userType) {
                case 1:
                    studentMenu(scanner, studentDAO, counselorDAO, appointmentDAO, bookingDAO);
                    break;
                case 2:
                    counselorMenu(scanner, counselorDAO, appointmentDAO, bookingDAO);
                    break;
                case 3:
                    System.out.println("Exiting the application");
                    break;
                default:
                    System.out.println("Invalid user type. Please try again.");
                    break;
            }
        } while (userType != 3);

        scanner.close();
    }

    private static void studentMenu(Scanner scanner, StudentInterface studentDAO, CounselorInterface counselorDAO,
                                     AppointmentInterface appointmentDAO, BookingInterface bookingDAO) {
        int choice;
        do {
            System.out.println("==== Student Menu ====");
            System.out.println("1. View All Students");
            System.out.println("2. Check Counselor Availability");
            System.out.println("3. View My Appointments");
            System.out.println("4. Book Appointment");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Add New Student");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            System.out.println();
            switch (choice) {
                case 1:
                    viewAllStudents(studentDAO);
                    break;
                case 2:
                    checkCounselorAvailability(scanner, counselorDAO, appointmentDAO);
                    break;
                case 3:
                    viewStudentAppointments(scanner, studentDAO, appointmentDAO);
                    break;
                case 4:
                    bookAppointment(scanner, studentDAO, counselorDAO, appointmentDAO, bookingDAO);
                    break;
                case 5:
                    cancelAppointment(scanner, studentDAO, appointmentDAO, bookingDAO);
                    break;
                case 6:
                    addNewStudent(scanner, studentDAO);
                    break;
                case 7:
                    System.out.println("Returning to User Type selection.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } 
        while (choice != 7);
    }

    private static void counselorMenu(Scanner scanner, CounselorInterface counselorDAO, AppointmentInterface appointmentDAO,
                                       BookingInterface bookingDAO) {
        int choice;
        do {
            System.out.println("==== Counselor Menu ====");
            System.out.println("1. View All Counselors");
            System.out.println("2. View My Appointments");
            System.out.println("3. Add New Counselor");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    viewAllCounselors(counselorDAO);
                    break;
                case 2:
                    viewCounselorAppointments(scanner, counselorDAO, appointmentDAO);
                    break;
                case 3:
                    addNewCounselor(scanner, counselorDAO);
                    break;
                case 4:
                    System.out.println("Returning to User Type selection.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } 
        while (choice != 4);
    }

    private static void viewAllStudents(StudentInterface studentDAO) {
        List<Student> students = studentDAO.getAllStudents();
		System.out.println("Students:");
		for (Student student : students) {
		    System.out.println(student);
		}
    }

    private static void checkCounselorAvailability(Scanner scanner, CounselorInterface counselorDAO,
                                                   AppointmentInterface appointmentDAO) {
        System.out.print("Enter day to check availability (e.g., Monday): ");
        scanner.nextLine();
        String dayToCheck = scanner.nextLine();
        System.out.print("Enter slot to check availability (e.g., Morning): ");
        String slotToCheck = scanner.nextLine();
        System.out.println();
        List<Counselor> availableCounselors = counselorDAO.getAvailableCounselors(dayToCheck, slotToCheck);
		if (availableCounselors.isEmpty()) {
		    System.out.println("No counselors available on " + dayToCheck + " " + slotToCheck + ".");
		} 
		else {
		    System.out.println("Available Counselors on " + dayToCheck + " " + slotToCheck + ":");
		    for (Counselor counselor : availableCounselors) {
		        System.out.println(counselor);
		        System.out.println();
		    }
		}
    }

    private static void viewStudentAppointments(Scanner scanner, StudentInterface studentDAO,
                                                AppointmentInterface appointmentDAO) {
        System.out.print("Enter student ID: ");
        int studentIdToView = scanner.nextInt();

        Student student = studentDAO.getStudentById(studentIdToView);
		if (student == null) {
		    System.out.println("Student with ID " + studentIdToView + " not found.");
		} else {
		    List<Appointment> studentAppointments = appointmentDAO.getAppointmentsByStudent(studentIdToView);
		    if (studentAppointments.isEmpty()) {
		        System.out.println("No appointments for student with ID: " + studentIdToView);
		    } else {
		        System.out.println("Appointments for student with ID " + studentIdToView + ":");
		        for (Appointment appointment : studentAppointments) {
		            System.out.println(appointment);
		            System.out.println();
		        }
		    }
		}
    }

    private static void bookAppointment(Scanner scanner, StudentInterface studentDAO, CounselorInterface counselorDAO,
         AppointmentInterface appointmentDAO, BookingInterface bookingDAO) {
			System.out.print("Enter your Student ID: ");
			int studentId = scanner.nextInt();
			
			Student student = studentDAO.getStudentById(studentId);
			if (student == null) {
				System.out.println("Student with ID " + studentId + " not found.");
				System.out.println();
				return;
			}
			
			System.out.print("Enter the day (e.g., Monday, Tuesday, etc.): ");
			scanner.nextLine();
			String day = scanner.nextLine();
			System.out.print("Enter the time slot (e.g., Morning, Afternoon, etc.): ");
			String slot = scanner.nextLine();
			System.out.println();
			
			List<Counselor> availableCounselors = counselorDAO.getAvailableCounselors(day, slot);
			if (availableCounselors.isEmpty()) {
				System.out.println("No available counselors for the specified day and slot.");
			}
			else {
				System.out.println("Available Counselors:");
			for (Counselor counselor : availableCounselors) {
				System.out.println(counselor);
				System.out.println();
			}
			
			System.out.print("Enter the ID of the counselor you want to book an appointment with: ");
			int counselorId = scanner.nextInt();
//			System.out.println();
			
			boolean isCounselorAvailable = false;
			for (Counselor counselor : availableCounselors) {
				if (counselor.getCounselorId() == counselorId) {
					isCounselorAvailable = true;
					break;
				}
			}
			
			if (!isCounselorAvailable) {
				System.out.println("The selected counselor is not available at the specified day and slot.");
				return;
			}
			else {
		        if (appointmentDAO.isSlotBooked(day, slot, counselorId)) {
		        	System.out.println("The selected slot is already booked. Please choose another slot.");
		        	return;
		        } else {
		        	Appointment appointment = new Appointment(0, slot, day, counselorId, studentId);
		        	appointmentDAO.saveOrUpdateAppointment(appointment);
		        	Booking booking = new Booking(0, day, slot, studentId, appointment.getAppointmentId());
		        	bookingDAO.saveOrUpdateBooking(booking);
//		        	System.out.println("Appointment booked successfully!");
		        	System.out.println();
		        }
		    }
			
//			if (appointmentDAO.isSlotBooked(day, slot, counselorId)) {
//			System.out.println("The selected slot is already booked. Please choose another slot.");
//			return;
//			}
			
						}
			}


    private static void cancelAppointment(Scanner scanner, StudentInterface studentDAO, AppointmentInterface appointmentDAO,
            BookingInterface bookingDAO) {
    		System.out.print("Enter your Student ID: ");
    		int studentId = scanner.nextInt();

    		Student student = studentDAO.getStudentById(studentId);
			if (student == null) {
				System.out.println("Student with ID " + studentId + " not found.");
				return;
			}

			System.out.print("Enter the ID of the appointment you want to cancel: ");
			int appointmentId = scanner.nextInt();
			System.out.println();
			
			Appointment appointmentToCancel = appointmentDAO.getAppointmentsById(appointmentId);
			if (appointmentToCancel == null || appointmentToCancel.getStudentId() != studentId) {
				System.out.println("Invalid appointment ID or the appointment does not belong to you.");
			} else {
				appointmentDAO.deleteAppointmentById(appointmentId);
				bookingDAO.deleteBookingByAppointmentId(appointmentId);
				System.out.println("Appointment canceled successfully!");
				System.out.println();
			}
    }

    private static void addNewStudent(Scanner scanner, StudentInterface studentDAO) {
        System.out.print("Enter student name: ");
        scanner.nextLine();
        String studentName = scanner.nextLine();
        System.out.print("Enter student email: ");
        String studentEmail = scanner.nextLine();
        Student newStudent = new Student(0, studentName, studentEmail);

        studentDAO.saveOrUpdateStudent(newStudent);
		System.out.println("New student added successfully.");
		System.out.println();
    }

    private static void viewAllCounselors(CounselorInterface counselorDAO) {
        List<Counselor> counselors = counselorDAO.getAllCounselors();
		System.out.println("Counselors:");
		for (Counselor counselor : counselors) {
		    System.out.println(counselor);
		    System.out.println();
		}
    }

    private static void viewCounselorAppointments(Scanner scanner, CounselorInterface counselorDAO,
                                                  AppointmentInterface appointmentDAO) {
        System.out.print("Enter counselor ID: ");
        int counselorIdToView = scanner.nextInt();

        Counselor counselor = counselorDAO.getCounselorById(counselorIdToView);
		if (counselor == null) {
		    System.out.println("Counselor with ID " + counselorIdToView + " not found.");
		} 
		else {
		    List<Appointment> counselorAppointments = appointmentDAO.getAppointmentsByCounselor(counselorIdToView);
		    if (counselorAppointments.isEmpty()) {
		        System.out.println("No appointments for counselor with ID: " + counselorIdToView);
		    } else {
		        System.out.println("Appointments for counselor with ID " + counselorIdToView + ":");
		        System.out.println();
		        for (Appointment appointment : counselorAppointments) {
		            System.out.println(appointment);
		            System.out.println();
		        }
		    }
		}
    }

    private static void addNewCounselor(Scanner scanner, CounselorInterface counselorDAO) {
        System.out.print("Enter counselor name: ");
        scanner.nextLine();
        String counselorName = scanner.nextLine();
        System.out.print("Enter counselor email: ");
        String counselorEmail = scanner.nextLine();
        Counselor newCounselor = new Counselor(0, counselorName, counselorEmail);

        counselorDAO.saveOrUpdateCounselor(newCounselor);
		System.out.println("New counselor added successfully.");
		System.out.println();
    }
}