from recommendations import *

# 欧几里得距离评价
print(sim_distance(critics, 'Lisa Rose', 'Gene Seymour'))

# 皮尔逊相关度评价，判断两组数据与某一直线拟合程度的一种度量。
print(sim_pearson(critics, 'Lisa Rose', 'Gene Seymour'))

# 返回相关性分值最高的TopN
print(top_matches(critics, 'Toby', n=5))
# 获取影片推荐
print(get_recommendations(critics, 'Toby'))
print(get_recommendations(critics, 'Toby', similarity=sim_distance))

print(transform_prefs(critics))
# 获取影片推荐
print(top_matches(transform_prefs(critics), 'Superman Returns'))
print(get_recommendations(transform_prefs(critics), 'Just My Luck'))

item_sim = calculate_similar_items(critics)
print(item_sim)
print(get_recommended_items(critics, item_sim, 'Toby'))
