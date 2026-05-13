package qa.automation.reporting.backend.html;

public final class Js {

    private Js() {
    }

    public static String render(
            int passedFeatures, int failedFeatures, int partialFeatures, int skippedFeatures,
            int passedScenarios, int failedScenarios, int partialScenarios, int skippedScenarios,
            int passedSteps, int failedSteps, int softFailSteps, int skippedSteps,
            int hardfailCount, int softFailcount, int warnCount
    ) {
        return String.format("""
                        <script>
                        
                        document.addEventListener('click', function (e) {
                            const header = e.target.closest('.dropdown-header');
                            if (!header) return;
                        
                            const dropdown = header.closest('.dropdown');
                            if (dropdown) {
                                dropdown.classList.toggle('open');
                            }
                        });
                        
                        function toggleGroup(groupId) {
                        
                            const scenarioName = groupId.replace(/_/g, ' ');
                            const cb = document.querySelector(
                                ".left input[onclick*=\\"" + groupId + "\\"]"
                            );
                            const show = cb && cb.checked;
                        
                            document.querySelectorAll("h3").forEach(h3 => {
                                const text = h3.textContent.trim();
                                if (text.startsWith(scenarioName)) {
                                    const container = h3.closest(".scenario");
                                    if (container) {
                                        container.style.display = show ? "block" : "none";
                                    }
                                }
                            });
                        }
                        
                        function pie(id, labels, data, colors){
                            const canvas = document.getElementById(id);
                            if (!canvas) return;
                        
                            new Chart(canvas,{
                                type:'pie',
                                data:{
                                    labels:labels,
                                    datasets:[{
                                        data:data,
                                        backgroundColor:colors,
                                        hoverOffset:14
                                    }]
                                },
                                options:{
                                    plugins:{
                                        legend:{ position:'bottom', labels:{ color:'white' }},
                                        datalabels:{
                                            color:'#fff',
                                            font:{weight:'bold'},
                                            formatter:(v,ctx)=>{
                                                const total =
                                                    ctx.dataset.data.reduce((a,b)=>a+b,0);
                                                return ((v/total)*100).toFixed(1)+'%%';
                                            }
                                        }
                                    }
                                },
                                plugins:[ChartDataLabels]
                            });
                        }
                        
                        
                        pie('featureChart',['Passed', 'Failed', 'Partial', 'Skipped'],
                          [%d, %d, %d, %d],
                          ['#2ecc71', '#e74c3c', "#9b59b6", "#7f8c8d"]
                        );
                        
                        pie('scenarioChart',['Passed','Failed','Partial','Skipped'],[%d,%d,%d,%d],['#2ecc71','#e74c3c','#9b59b6','#7f8c8d']);
                        pie('stepChart',['Passed','Failed','Soft Fail','Skipped'],
                            [%d,%d,%d,%d],
                            ['#2ecc71','#e74c3c','#9b59b6','#7f8c8d']);
                        pie('defectChart',['Hard Fail','Soft Fail','Warning'],
                            [%d,%d,%d],
                            ['#e74c3c','#9b59b6','#f39c12']);
                        
                        </script>
                        <script>
                        function openLightbox(src) {
                            var overlay = document.getElementById("lightbox-overlay");
                            var img = document.getElementById("lightbox-image");
                            img.src = src;
                            overlay.style.display = "block";
                        }
                        
                        function closeLightbox() {
                            var overlay = document.getElementById("lightbox-overlay");
                            overlay.style.display = "none";
                        }
                        </script>
                        """,
                passedFeatures, failedFeatures, partialFeatures, skippedFeatures,
                passedScenarios, failedScenarios, partialScenarios, skippedScenarios,
                passedSteps, failedSteps, softFailSteps, skippedSteps,
                hardfailCount, softFailcount, warnCount
        );
    }
}