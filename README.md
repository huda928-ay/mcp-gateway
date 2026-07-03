# MCP Gateway Application

Bu proje, Model Context Protocol (MCP) sunucularının sağlık durumlarını dinamik olarak izleyen ve yönetim arayüzü sunan bir Spring Boot ağ geçidi (Gateway) uygulamasıdır.

## Gereksinimler
* Java 17 veya üzeri
* Maven 3.6+
* Modern bir web tarayıcısı

## Kurulum ve Çalıştırma Adımları

### 1. Projeyi Derleyin (Build)
Uygulama dizininde terminali açarak bağımlılıkları yüklemek ve projeyi paketlemek için aşağıdaki komutu çalıştırın:
```bash
mvn clean package -DskipTests
2. Uygulamayı Başlatın (Run)
Derleme işlemi bittikten sonra uygulamayı ayağa kaldırmak için:

Bash
mvn spring-boot:run
Uygulama varsayılan olarak 8080 portunda çalışacaktır.

 Kullanım / Endpointler
Yönetim Paneli (GUI): http://localhost:8080 (Arayüz üzerinden sunucuları canlı izleyebilir ve anlık URL güncelleyebilirsiniz).

H2 Konsolu (Veritabanı): http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:mcpdb

Username: sa | Password: password

 Testleri Çalıştırma
Yazılan JUnit birim testlerini koşturmak için:

Bash
mvn test

---

###  Kabul Kriteri Kontrolü (DoD)

* [x] **JUnit Testleri:** Temel başarı ve hata fırlatma senaryoları kapsandı.
* [x] **Canlı GUI Simülasyonu:** Yarış durumları (Race Condition) ve Timeout mimarisi çözüldüğü için artık kusursuz işliyor.
* [x] **Dokümantasyon:** `README.md` ayağa kaldırma adımlarıyla netleştirildi.

