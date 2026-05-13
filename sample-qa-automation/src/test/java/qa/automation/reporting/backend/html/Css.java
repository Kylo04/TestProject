package qa.automation.reporting.backend.html;

public final class Css {

    private Css() {
    }

    public static final String BLOCK = """
            body {
              margin: 0;
              font-family: Segoe UI, Arial;
              background: #f4f6f8;
              display: flex;
              flex-direction: column;
              min-height: 100vh;
            }
            
            h2, h3 {
              margin: 6px 0;
            }
            
            .passed  { color: #2ecc71; }
            .failed  { color: #e74c3c; }
            .warning { color: #f39c12; }
            .partial { color: #9b59b6; }
            
            .dashboard {
              background: #1e1e2f;
              color: white;
              padding: 15px;
            }
            
            .dashboard-grid {
              display: grid;
              grid-template-columns: repeat(4, 1fr);
              gap: 15px;
            }
            
            .card {
              background: #2a2a40;
              padding: 12px;
              border-radius: 8px;
              box-shadow:
                inset 0 -10px 15px rgba(0,0,0,0.7),
                0 10px 20px rgba(0,0,0,0.7);
            }
            
            .chart-wrap {
              width: 250px;
              height: 250px;
              margin: 10px auto 0;
            }
            
            .chart-wrap canvas {
              width: 100% !important;
              height: 100% !important;
            }
            
            .card h3 {
              margin-bottom: 6px;
            }
            
            .container {
              display: flex;
              flex: 1;
              min-height: 0;
            }
            
            .left,
            .right {
              height: 100%;
              overflow-y: auto;
            }
            
            .left {
              width: 35%;
              background: #ffffff;
              border-right: 1px solid #ccc;
              overflow: auto;
            }
            
            .left label {
              display: block;
              margin: 4px 6px;
            }
            
            .left {
                position: relative;
                width: 450px;
                min-height: 100vh;
                overflow-y: auto;
                z-index: 0;
            }
            
            .left::before {
                content: "";
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
                opacity: 0.8;
                z-index: -1;
            }
            
            .dashboard .metric {
              display: flex !important;
              flex-direction: row !important;
              gap: 20px !important;
              flex-wrap: wrap;
              margin-bottom: 20px;
            }
            
            .dashboard .metric {
              margin-bottom: 8px;
            }
            
            .metric-row strong {
              color: #000000;
            }
            
            .metric-row {
              background-color: rgb(204, 244, 26);
              color: #000000;
              padding: 10px 15px;
              border-radius: 4px;
              flex: 0 1 auto;
            }
            
            .right {
              width: 90%;
              padding: 10px;
              overflow: auto;
            }
            
            .dropdown {
              border: 1px solid #ccc;
              border-radius: 6px;
              margin: 6px;
              padding: 6px;
            }
            
            .dropdown-header {
              cursor: pointer;
              font-weight: bold;
            }
            
            .dropdown-content {
              display: none;
              margin-top: 6px;
            }
            
            .dropdown.open .dropdown-content {
              display: block;
            }
            
            .step {
              margin-left: 20px;
            }
            
            .log {
              margin-left: 40px;
              font-size: 13px;
            }
            
            .arrow {
              display: inline-block;
              width: 16px;
              transition: transform 0.2s ease;
            }
            
            .dropdown.open .arrow {
              transform: rotate(90deg);
            }
            
            .arrow {
              transition: transform 0.25s cubic-bezier(.4,0,.2,1);
            }
            
            .log-screenshot {
              margin-left: 45px;
              margin-top: 4px;
            }
            
            .log-screenshot img {
              max-width: 180px;
              max-height: 120px;
              border: 1px solid #444;
              border-radius: 4px;
              cursor: pointer;
            }
            
            .log.info {
              color: #3498db;        /* Blue (INFO) */
            }
            
            .log.passed {
              color: #2ecc71;        /* Green */
            }
            
            .log.warning {
              color: #f39c12;        /* Amber (WARN) */
            }
            
            .log.partial {
              color: #9b59b6;        /* Purple (SOFT_FAIL) */
            }
            
            .log.failed {
              color: #e74c3c;        /* Red */
            }
            
            
            .log.skipped {
                color: #7f8c8d;     /* Grey */
            }
            
            .skipped {
                color: #7f8c8d;
            }
            
            .screenshot-thumb {
              max-width: 250px;
              border: 1px solid #ccc;
              margin-top: 6px;
              cursor: pointer;
              transition: transform 0.15s ease-in-out;
            }
            
            .screenshot-thumb:hover {
              transform: scale(1.02);
            }
            
            #lightbox-overlay {
              display: none;
              position: fixed;
              z-index: 10000;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              background: rgba(0, 0, 0, 0.85);
              text-align: center;
            }
            
            #lightbox-overlay img {
              max-width: 95%;
              max-height: 95%;
              margin-top: 2%;
            }
            
            """;
}