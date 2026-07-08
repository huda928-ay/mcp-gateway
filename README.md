# MCP Gateway & Management Console

Bu proje, Model Context Protocol (MCP) sunucularının sağlık durumlarını dinamik olarak izleyen, bunları **LangChain4j** ile akıllı yapay zeka araçlarına (Tool) dönüştüren ve otonom iş akışları (Agentic Workflow) yürüten akıllı bir Spring Boot ağ geçidi uygulamasıdır.

## Öne Çıkan Özellikler (2. Hafta)
* **Akıllı Ajan Katmanı:** Konuşma bağlamını koruyan hafızalı (`ChatMemory`) yapay zeka entegrasyonu.
* **Dinamik Function Calling:** Durumu `UP` olan sunucuların (`Kubernetes`, `Grafana`, `Nexus`) semantik açıklamalarla otonom Java metot tetikleyicilerine dönüştürülmesi.
* **Ajantik İş Akışı Modeli:** Sıralı adımları `@OrderColumn` ile veritabanında saklayan ilişkisel mimari.
* **Workflow GUI & Canlı Takip:** Kutular ve oklardan oluşan, arka plandaki gerçek ajan loglarıyla senkronize çalışan dinamik akış simülatörü (Çalışıyor: Sarı, Başarılı: Yeşil).

## Kurulum ve Çalıştırma Adımları

### 1. Projeyi Derleyin (Build)
mvn clean package -DskipTests
2. Uygulamayı Başlatın (Run)
Bash
mvn spring-boot:run
Uygulama varsayılan olarak http://localhost:8080 portunda çalışacaktır.

 Kullanım / Endpointler
Yönetim Paneli & GUI: http://localhost:8080 (Sunucu izleme, Chatbot ve Akış Görselleştirme tek ekrandadır).

İş Akışı API'leri: * POST /api/workflows -> Yeni sıralı akış kaydeder.

GET /api/workflows -> Kayıtlı akışları listeler.

POST /api/workflows/execute-step -> İlgili adımı gerçek ajana yürüttürür.

H2 Konsolu (Veritabanı): http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:mcpdb | User: sa | Password: password

 Testleri Çalıştırma
 mvn test

 Kabul Kriteri Kontrolü (DoD)
[x] JUnit Testleri: Temel başarı ve hata fırlatma senaryoları kapsandı.

[x] Canlı GUI & Ajan Entegrasyonu: İş akışları hem arayüzden renkli olarak hem de backend terminalinden gerçek zamanlı Function Calling loglarıyla doğrulanabiliyor.
