# How to Run JUnit 5 Tests - DevVinh

## Prerequisites

- Java 21+
- Maven 3.6+
- Git

## Test Files Location

```
src/test/java/poly/edu/java5_asm/DevVinh/
├── ProductServiceTestJUnit5.java
└── CartServiceTestJUnit5.java
```

---

## Running Tests

### Option 1: Run All JUnit 5 Tests (Recommended)

```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

**Expected Output:**
```
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

### Option 2: Run ProductService Tests Only

```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5"
```

**Expected Output:**
```
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

### Option 3: Run CartService Tests Only

```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.CartServiceTestJUnit5"
```

**Expected Output:**
```
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

### Option 4: Run Specific Test Method

```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5#testGetLatestProducts_Success"
```

---

### Option 5: Run with Code Coverage Report

```bash
mvn clean test jacoco:report
```

**Report Location:**
```
target/site/jacoco/index.html
```

---

## Using Command Prompt (CMD) on Windows

If you're using PowerShell and getting errors, use Command Prompt instead:

1. Press `Win + R`
2. Type `cmd` and press Enter
3. Navigate to project folder:
   ```cmd
   cd C:\path\to\fatc-Grocery-Store
   ```
4. Run tests:
   ```cmd
   mvn test -Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5,poly.edu.java5_asm.DevVinh.CartServiceTestJUnit5
   ```

---

## Using PowerShell on Windows

If using PowerShell, use quotes properly:

```powershell
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

Or use single quotes:

```powershell
mvn test '-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5'
```

---

## Test Results Interpretation

### Success Output
```
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
✅ All tests passed

### Failure Output
```
[INFO] Tests run: 28, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```
❌ One or more tests failed

### Error Output
```
[INFO] Tests run: 28, Failures: 0, Errors: 1, Skipped: 0
[INFO] BUILD FAILURE
```
❌ Compilation or runtime error

---

## Viewing Test Details

### Verbose Output
```bash
mvn test -X "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

### Show Test Names
```bash
mvn test -v "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

---

## Test Execution Flow

```
1. Maven compiles test classes
2. Mockito initializes mocks
3. @BeforeEach runs setup
4. Each @Test method executes
5. Assertions verify results
6. Test report generated
```

---

## Common Issues & Solutions

### Issue 1: "Unknown lifecycle phase"
**Cause**: PowerShell interpreting special characters  
**Solution**: Use quotes or switch to CMD
```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

### Issue 2: "Tests not found"
**Cause**: Wrong class name or path  
**Solution**: Verify class name matches exactly
```bash
# Correct
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5"

# Wrong
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTest"
```

### Issue 3: "Compilation errors"
**Cause**: Missing dependencies or syntax errors  
**Solution**: Run clean build
```bash
mvn clean compile test
```

### Issue 4: "Timeout"
**Cause**: Tests taking too long  
**Solution**: Increase timeout
```bash
mvn test -DargLine="-Xmx1024m" "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

---

## Test Statistics

| Metric | Value |
|--------|-------|
| Total Tests | 28 |
| ProductService | 12 |
| CartService | 16 |
| Success Cases | 24 |
| Exception Cases | 4 |
| Expected Duration | < 5 seconds |

---

## IDE Integration

### IntelliJ IDEA
1. Right-click on test class
2. Select "Run 'ClassName'"
3. Or press `Ctrl + Shift + F10`

### Eclipse
1. Right-click on test class
2. Select "Run As" → "JUnit Test"
3. Or press `Alt + Shift + X, T`

### VS Code
1. Install "Test Explorer UI" extension
2. Click on test in explorer
3. Click "Run" button

---

## Continuous Integration

### GitHub Actions
```yaml
- name: Run Tests
  run: mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

### Jenkins
```groovy
stage('Test') {
    steps {
        sh 'mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"'
    }
}
```

### GitLab CI
```yaml
test:
  script:
    - mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

---

## Performance Tips

### Run Tests in Parallel
```bash
mvn test -T 1C "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

### Skip Tests During Build
```bash
mvn clean package -DskipTests
```

### Run Only Failed Tests
```bash
mvn test -Dtest=failingTestClass
```

---

## Debugging Tests

### Add Breakpoints
1. Open test class in IDE
2. Click on line number to add breakpoint
3. Run test in debug mode
4. Step through code

### Print Debug Info
```java
System.out.println("Debug: " + variable);
```

### Use Logging
```java
log.debug("Debug message");
log.info("Info message");
```

---

## Test Report Generation

### Generate HTML Report
```bash
mvn clean test jacoco:report
```

### View Report
```
target/site/jacoco/index.html
```

### Generate Surefire Report
```bash
mvn surefire-report:report
```

### View Surefire Report
```
target/site/surefire-report.html
```

---

## Best Practices

✅ **Do**
- Run tests before committing
- Run full test suite in CI/CD
- Keep tests isolated
- Use meaningful test names
- Mock external dependencies

❌ **Don't**
- Modify test data during tests
- Create dependencies between tests
- Use hardcoded values
- Skip failing tests
- Ignore test failures

---

## Support

For issues or questions:
1. Check test documentation in `README_JUNIT5.md`
2. Review test code comments
3. Check Maven output for error messages
4. Consult team lead

---

## Quick Reference

```bash
# Run all tests
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"

# Run ProductService tests
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5"

# Run CartService tests
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.CartServiceTestJUnit5"

# Run with coverage
mvn clean test jacoco:report

# Run specific test
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5#testGetLatestProducts_Success"

# Clean and rebuild
mvn clean compile test
```

---

**Status**: ✅ Ready to Execute  
**Last Updated**: February 26, 2026
